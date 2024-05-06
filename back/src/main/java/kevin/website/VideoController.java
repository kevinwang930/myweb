package kevin.website;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kevin.website.model.Range;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.nio.file.Files;

@Controller
public class VideoController {


    @GetMapping(path = "/video")
    public void video(HttpServletRequest request, HttpServletResponse response) {
        String range = request.getHeader(HttpHeaders.RANGE);
        try (
                InputStream is = new ClassPathResource("video/video_sample.mp4").getInputStream();
                OutputStream os = response.getOutputStream();
        ) {
            long start = 0, end = is.available()-1;
            long fileLength = is.available();
        if (range != null) {
            String[] splits = range.split("=");
            if (splits.length > 1) {
                String[] ranges = splits[1].split("-");
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    end = Long.parseLong(ranges[1]);
                }
            }
            long contentLength = end -start +1;
            response.setHeader(HttpHeaders.CONTENT_RANGE,
                               "bytes " + start + "-" + end  + "/" + fileLength);
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        } else {
            response.setHeader(HttpHeaders.CONTENT_RANGE, String.valueOf(fileLength));
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
        }
        response.setContentType("video/mp4");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Range, X-Requested-With, Content-Type, " +
                    "Accept");

            byte[] buffer = new byte[1024];
            if (start > 0) {
                is.skip(start);
            }
            int flag;
            while ((flag = is.read(buffer)) != -1) {
                os.write(buffer, 0, flag);
            }
            os.flush();
            response.setStatus(206);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


