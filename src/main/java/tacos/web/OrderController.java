package tacos.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import tacos.data.OrderRepository;
import tacos.model.Order;
import tacos.model.User;

import javax.validation.Valid;


@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {
  
  private OrderRepository orderRepo;
  
  public OrderController(OrderRepository orderRepo) {
    this.orderRepo = orderRepo;
  }
  
  
  @GetMapping("/current")
  public String orderForm() {
    return "orderForm";
  }
  
  /*
  Authentication authentication =
      SecurityContextHolder.getContext().getAuthentication();
  User user = (User) authentication.getPrincipal();
  
  Although this snippet is thick with security-specific code,
  it has one advantage over the other approaches described:
  it can be used anywhere in the application, not only in a controller’s handler methods.
  */
  
  @PostMapping
  public String processOrder(@Valid Order order, Errors errors,
                             SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
    
    if (errors.hasErrors()) return "orderForm";
    
    order.setUser(user);
    
    orderRepo.save(order);
    
    sessionStatus.setComplete();
    
    
    log.info("Order submitted: " + order);
    return "redirect:/";
  }
}
