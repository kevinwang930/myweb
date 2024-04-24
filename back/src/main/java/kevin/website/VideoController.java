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

@Controller
public class VideoController {


    @GetMapping(path = "/video")
    public void video(HttpServletRequest request, HttpServletResponse response) {
        Range range = getRange(request);
        try (
                InputStream is = new ClassPathResource("video/video_sample.mp4").getInputStream();
                OutputStream os = response.getOutputStream();
        ) {
            int fileSize = is.available();
            byte[] buffer = new byte[1024];
            response.setContentType("video/mp4");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + fileSize);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Range, X-Requested-With, Content-Type, " +
                    "Accept");
            response.setHeader(HttpHeaders.CONTENT_RANGE,
                               "bytes=" + range.getStart() + "-" + (range.getEnd() > range.getStart() ?
                                       range.getEnd() :
                                       fileSize - 1) + "/" + fileSize);
            long rangeStart = range.getStart();
            if (rangeStart > 0) {
                is.skip(rangeStart);
            }
            int flag;
            while ((flag = is.read(buffer)) != -1) {
                os.write(buffer, 0, flag);
            }
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Range getRange(HttpServletRequest request) {
        String rangeHeader = request.getHeader(HttpHeaders.RANGE);
        long rangeStart = 0, rangeEnd = 0;
        if (rangeHeader != null) {
            String[] splits = rangeHeader.split("=");
            if (splits.length > 1) {
                String[] ranges = splits[1].split("-");
                rangeStart = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    rangeEnd = Long.parseLong(ranges[1]);
                }
            }
        }
        return new Range(rangeStart, rangeEnd);
    }
}


