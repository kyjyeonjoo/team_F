package ce.mun.siteuser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ce.mun.siteuser.domain.ArticleDTO;
import ce.mun.siteuser.domain.Article_reviewDTO;
import ce.mun.siteuser.domain.FileDTO;
import ce.mun.siteuser.repository.Article;
import ce.mun.siteuser.repository.ArticleHeader;
import ce.mun.siteuser.repository.Article_review;
import ce.mun.siteuser.repository.Article_reviewHeader;
import ce.mun.siteuser.repository.FileRepository;
import ce.mun.siteuser.repository.SiteFile;
import ce.mun.siteuser.repository.SiteUser;
import ce.mun.siteuser.service.SiteUserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/siteuser/*")
public class ArticleController {
	
	@Autowired
	private SiteUserService userService;
	
	//파일 업로드 관련 내용
	@Autowired
	private FileRepository fileRepository;
	
	@GetMapping("bbs/upload")
	public String visitUpload() {
		return "/bbs/upload_form";
	}
	
	@PostMapping("bbs/upload")
	public String upload(@RequestParam MultipartFile file, Model model) throws IllegalStateException, IOException {
		if(!file.isEmpty()) {
			String fileName = file.getOriginalFilename();//업로드 파일 명
			fileName = fileName.replace(" ", "_");
			FileDTO dto = new FileDTO();
			dto.setFileName(fileName);
			dto.setContentType(file.getContentType());
			File upfile = new File(dto.getFileName());
			file.transferTo(upfile);//파일 저장
			
			SiteFile siteFile = new SiteFile();
			siteFile.setName(fileName);
			fileRepository.save(siteFile); //파일 저장
			model.addAttribute("file", dto);
			}
			
		return "/bbs/result";
	}
	

	
	@GetMapping("bbs/filelist")
	public String filelist(Model model) {
		Iterable<SiteFile> files = fileRepository.findAll();
		model.addAttribute("files", files);
		return "/bbs/file_list";
	}
	
	
	
	@Value("${spring.servlet.multipart.location}")
	String base;
	@GetMapping("bbs/download")
	public ResponseEntity <Resource> download(FileDTO dto) throws IOException{
		Path path = Paths.get(base+"/"+dto.getFileName());
		
		
		String contentType = Files.probeContentType(path);
		HttpHeaders headers = new HttpHeaders();
		Resource res = new InputStreamResource(Files.newInputStream(path));
		
		if (dto.getFileName().endsWith(".png") || dto.getFileName().endsWith(".jpg") || dto.getFileName().endsWith(".jpeg") || dto.getFileName().endsWith(".gif")) {
			headers.add(HttpHeaders.CONTENT_TYPE, contentType);
			return new ResponseEntity<>(res, headers, HttpStatus.OK);
	    }

		headers.setContentDisposition(
				ContentDisposition.builder("attachment")
				.filename(dto.getFileName(), StandardCharsets.UTF_8).build());
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
			
		
	}
//////////////////////////////////////////////////////////////////////////////////////////////

	// 게시판 관련 내용(맛집)
	@GetMapping("board/write")
	public String bbsForm() {
		return "/board/restaurant_new_article";
	}
	@PostMapping("board/write")
	public String addArticle(ArticleDTO dto) {
		userService.save(dto);
		return "/board/restaurant_saved";
	}
	@GetMapping("board/read")
	public String readArticle(@RequestParam(name="num")Long num, @RequestParam( defaultValue="0",  name="pno") int pno, Model model, HttpSession session) {
		Article article = userService.getArticle(num);
		model.addAttribute("article", article);
		model.addAttribute("pno", pno);
		return "/board/restaurant_article";
	}
	
	
	@GetMapping("board/allarticles")
	public String getAllArticles(@RequestParam( defaultValue="0",  name="pno") int pno, Model model, HttpSession session, RedirectAttributes rd){
		String email = (String)session.getAttribute("email");
		if(email != null) {
			rd.addFlashAttribute("reason", "login required");
			return "redirect:/error";
		}
		
		int pageSize = 2;
		Pageable paging = PageRequest.of(pno, pageSize, Sort.Direction.DESC, "num"); 
		Page <ArticleHeader> list = userService.getArticleHeaders(paging);
		model.addAttribute("articles", list);
		return "/board/restaurant_articles";
	}
	

	@GetMapping("board")
	public String subjectArticle() {
		return "/board/subject";
	}
	
	// 수정 폼 이동
	@GetMapping("/board/edit")
	public String editArticleForm(@RequestParam Long num, Model model) {
		Article article = userService.getArticle(num); 
	    model.addAttribute("article", article);
	    return "/board/restaurant_edit_article";
	}

	// 수정 처리
	@PostMapping("/board/edit")
	public String editArticle(@RequestParam Long num, @RequestParam String title, @RequestParam String contents) {
		userService.edit(num, title, contents);
	    return "redirect:/siteuser/board/read?num=" + num;
	}

	// 삭제 처리
	@GetMapping("/board/delete")
	public String deleteArticle(@RequestParam Long num) {
		userService.delete(num);
	    return "redirect:/siteuser/board/restaurant_allarticles";
	}

	
//////////////////////////////////////////////////////////////////////////////////////////////

	// 게시판 관련 내용(후기)
	
	//쓰기
	@GetMapping("board/review_write")
	public String Form() {
		return "/board/review_new_article";
	}
	@PostMapping("board/review_write")
	public String addArticle_review(Article_reviewDTO dto) {
		userService.review_save(dto);
		return "/board/review_saved";
	}
	
	// 읽기
	@GetMapping("board/review_read")
	public String readArticle_review(@RequestParam(name="num")Long num, @RequestParam( defaultValue="0",  name="pno") int pno, Model model, HttpSession session) {
		Article_review article_review= userService.getArticle_review(num);
		model.addAttribute("article_review", article_review);
		model.addAttribute("pno", pno);
		return "/board/review_article";
	}
	

	@GetMapping("board/review_allarticles")
	public String getAllArticles_review(@RequestParam(defaultValue="0",  name="pno") int pno, Model model, HttpSession session, RedirectAttributes rd){
		String email = (String)session.getAttribute("email");
		if(email != null) {
			rd.addFlashAttribute("reason", "login required");
			return "redirect:/error";
		}
		
		
		int pageSize = 2;
		Pageable paging = PageRequest.of(pno, pageSize, Sort.Direction.DESC, "num"); 
		Page <Article_reviewHeader> list = userService.getArticle_reviewHeaders(paging);
		System.out.println("리뷰 게시글 개수: " + list.getTotalElements()); // 로그 확인
		
		model.addAttribute("review_articles", list);
		return "/board/review_articles";
	}
	
	// 수정 폼 이동
	@GetMapping("/board/review_edit")
	public String editReviewForm(@RequestParam Long num, Model model) {
		Article_review article_review = userService.getArticle_review(num); 
	    model.addAttribute("article_review", article_review);
	    return "/board/review_edit_article";
	}

	// 수정 처리
	@PostMapping("/board/review_edit")
	public String editReview(@RequestParam Long num, @RequestParam String title, @RequestParam String contents) {
		userService.editReview(num, title, contents);
	    return "redirect:/siteuser/board/review_read?num=" + num;
	}

	// 삭제 처리
	@GetMapping("/board/review_delete")
	public String deleteReview(@RequestParam Long num) {
		userService.deleteReview(num);
	    return "redirect:/siteuser/board/review_allarticles";
	}

	

	
	
}
