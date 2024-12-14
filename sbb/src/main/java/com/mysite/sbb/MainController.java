package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

	@GetMapping("/")
	public String root() {
		return "redirect:/question/list";
	}

//	**redirect**는 클라이언트 측에서 다시 새로운 요청을 서버로 보내게 하는 방식임.
//
//	왜 굳이 redirect를 쓸까?
//	
//	return "/question/list";
//	=> 이 경우는 단순히 **뷰(View)**를 렌더링하는 거암.
//	   Spring은 /question/list 경로와 관련된 HTML 템플릿을 찾아서 
//     그 페이지를 사용자에게 보여줌.
//	즉, 서버에서 **특정 템플릿 파일(HTML 파일)**을 사용해 그 페이지를 렌더링하고,
//	사용자에게 결과를 반환하는 방식임
//
//  URL 유지: redirect는 클라이언트가 새로운 요청을 보내는 방식이기 때문에, 
//  사용자의 브라우저 URL이 변경됨. 
//	즉, 최종적으로 **"/question/list"**로 이동한 것이 명확하게 보임.
//	하지만 ***뷰만 반환하면*** URL은 바뀌지 않고 그대로 **"/"**로 남아있을 거임.

}
