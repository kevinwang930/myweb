package kevin.website;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
public class VideoController {


    @GetMapping(path = "/video")
    public void video(HttpServletRequest request, HttpServletResponse response) {
        String range = request.getHeader(HttpHeaders.RANGE);
        response.setStatus(206);
        try (
                InputStream is = new ClassPathResource("video/video_sample.mp4").getInputStream();
                OutputStream os = response.getOutputStream();
        ) {
            long start = 0, end = is.available() - 1;
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
                long contentLength = end - start + 1;
                response.setHeader(HttpHeaders.CONTENT_RANGE,
                        "bytes " + start + "-" + end + "/" + fileLength);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/videoV2")
    public void videoV2(HttpServletRequest request, HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream();
             InputStream is = new ClassPathResource("video/video_sample.mp4").getInputStream()) {
            response.setContentType("video/mp4");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Range, X-Requested-With, Content-Type, " +
                    "Accept");
                response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(is.available()));

            IOUtils.copyLarge(is, os);



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path="/images")
    public ResponseEntity<String> images(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                // Process each file here
                System.out.println("Received file: " + file.getOriginalFilename() + " with size: " + file.getSize());
                // You might want to save it, analyze it, etc.
            }
            return ResponseEntity.ok("Successfully uploaded " + files.length + " files.");
        } else {
            return ResponseEntity.badRequest().body("No files were uploaded.");
        }

    }



}


