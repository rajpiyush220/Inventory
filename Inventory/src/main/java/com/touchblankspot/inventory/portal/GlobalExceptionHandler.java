package com.touchblankspot.inventory.portal;

import static com.touchblankspot.common.Constant.DEFAULT_ERROR_VIEW;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
      throw e;
    }

    ModelAndView mav = new ModelAndView();
    mav.addObject("exception", e);
    mav.addObject("url", req.getRequestURL());
    mav.setViewName(DEFAULT_ERROR_VIEW);
    return mav;
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  public ModelAndView accessDeniedHandler(HttpServletRequest req, Exception e) throws Exception {
    if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
      throw e;
    }

    ModelAndView mav = new ModelAndView();
    mav.addObject("exception", e);
    mav.addObject("url", req.getRequestURL());
    mav.setViewName("accessDenied");
    return mav;
  }
}
