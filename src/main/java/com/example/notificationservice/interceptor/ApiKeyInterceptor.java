package com.example.notificationservice.interceptor;



import com.example.notificationservice.entity.dto.ErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String API_KEY_HEADER = "Key";
    private static final String EXPECTED_API_KEY = "Meesho1234@";

    @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Extract the API key from the request headers
        String apiKey = request.getHeader(API_KEY_HEADER);

        // Check if the API key is missing or invalid
        if (apiKey == null || !apiKey.equals(EXPECTED_API_KEY)) {
            // Return 401 Unauthorized status if the API key is missing or invalid
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            ErrorDetail errorDetail=new ErrorDetail(new ErrorDetail.ErrorInfo("UnAuthorized User", "Authorization Error: You are not authorized to access this resource"));

            response.getWriter().write("Authorization Error: You are not authorized to access this resource");
            response.getWriter().flush();

            return false;

        }


        // API key is valid, allow the request to proceed
        return  true;
    }
}
