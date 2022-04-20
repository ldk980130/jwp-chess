package chess.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringChessController {

    @GetMapping("/")
    public String index() {
        return "login.html";
    }
}
