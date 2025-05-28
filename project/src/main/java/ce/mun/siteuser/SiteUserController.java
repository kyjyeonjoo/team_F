package ce.mun.siteuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ce.mun.siteuser.domain.SiteUserDTO;
import ce.mun.siteuser.repository.SiteUser;
import ce.mun.siteuser.service.SiteUserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/siteuser/*")
public class SiteUserController {
	@Autowired
	private SiteUserService userService;
	
	@GetMapping("login/signup")
	public String singup() {
		return "/login/signup_input";
	}
	@PostMapping("login/signup")
	public String signup(SiteUserDTO user) {
		userService.save(user);
		return "/login/signup_done";
	}
	@GetMapping("login/all")
	@ResponseBody
	public Iterable<SiteUser> getAllUsers(){
		return userService.getAll();
	}
	@GetMapping("login/all2")
	public String getAllUsers(Model model) {
		 Iterable<SiteUser> users = userService.getAll();
		 model.addAttribute("users", users);
		 return "/login/user_list";
	}
	@GetMapping("login/find")
	public String find() {
		return "/login/find_user";
	}
	@PostMapping("login/find")
	public String findUser(@RequestParam(name="email") String email, Model model, RedirectAttributes rd ) {
		SiteUser user = userService.getEmail(email);
		if(user != null) {
			model.addAttribute("user", user);
			return "/login/find_done";
		}
		rd.addFlashAttribute("reason", "wrong email");
		return "redirect:/error";
	}
	
	
	
	
	@GetMapping("/")
	public String start() {
		return "main";
	}
	@GetMapping("login/login")
	public String loginform() {
		return "/login/login";
	}
	@GetMapping("login/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "/login/login";
	}
	@PostMapping("login/login")
	public String loginUser(SiteUserDTO dto, HttpSession session, RedirectAttributes rd) {
		SiteUser user = userService.getEmail(dto.getEmail());
		if(user != null) {
			if(dto.getPasswd().equals(user.getPasswd())) {
				session.setAttribute("email", dto.getEmail());
				session.setAttribute("name", dto.getName());
				return "/login/login_done";
			}
		}
		rd.addFlashAttribute("reason", "wrong password");
		return "redirect:/error";
	}
		
	
	@GetMapping("login/login_done")
	public String loginDonePage() {
	    return "/login/login_done";
	}

	@PostMapping("login/update")
	public String updateUserInfo(@RequestParam String email,
	                             @RequestParam String password,
	                             @RequestParam String phone,
	                             @RequestParam String address,
	                             HttpSession session,
	                             RedirectAttributes rd) {
	    SiteUser user = userService.getEmail(email);
	    if (user != null) {
	        userService.updateName(email, user.getName(), password, phone, address);
	        
	        // 세션 정보도 업데이트
	        session.setAttribute("passwd", password);
	        session.setAttribute("phone", phone);
	        session.setAttribute("address", address);
	        
	        rd.addFlashAttribute("msg", "정보가 성공적으로 변경되었습니다.");
	        return "redirect:/siteuser/login_done";
	    }
	    rd.addFlashAttribute("reason", "사용자를 찾을 수 없습니다.");
	    return "redirect:/error";
	}


	/*
		@PostMapping("/update")
	public String updateUserInfo(SiteUserDTO dto,
	                             HttpSession session,
	                             RedirectAttributes rd) {
	    SiteUser user = userService.getEmail(dto.getEmail());
	    if (user != null) {
	        userService.updateName(dto.getName(), dto.getEmail(), dto.getPasswd(), dto.getPhone(), dto.getAddress());
	        
			session.setAttribute("passwd", dto.getPasswd());
			session.setAttribute("phone", dto.getPhone());
			session.setAttribute("address", dto.getAddress());
			
	        rd.addFlashAttribute("msg", "정보가 성공적으로 변경되었습니다.");
	        return "redirect:/siteuser/login_done";
	    }
	    rd.addFlashAttribute("reason", "사용자를 찾을 수 없습니다.");
	    return "redirect:/error";
	}
	
	@PostMapping("/login_done")
	public String updateName(SiteUserDTO dto, HttpSession session, RedirectAttributes rd) {
		SiteUser user = userService.getEmail(dto.getEmail());
		if(user != null) {
			if(dto.getPasswd().equals(user.getPasswd())) {
				userService.updateName(dto.getName(), dto.getEmail(), dto.getPasswd(), dto.getPhone(), dto.getAddress());
				session.setAttribute("name", dto.getName());
				session.setAttribute("email", dto.getEmail());
				session.setAttribute("passwd", dto.getPasswd());
				session.setAttribute("phone", dto.getPhone());
				session.setAttribute("address", dto.getAddress());
				
				return "redirect:/login_done";
			}
		}
		return "redirect:/error";
	}
	*/
	
	
	
	
	/*
	@PostMapping("/login")
	public String startpage(SiteUserDTO dto, HttpSession session, RedirectAttributes rd) {
		SiteUser user = userService.getName(dto.getName());
		if(user != null) {
			if(dto.getPasswd().equals(user.getPasswd())) {
				session.setAttribute("name", dto.getName());
				return "start";
			}
		}
		rd.addFlashAttribute("reason", "wrong password");
		return "redirect:/error";
	}
	*/
	
}
