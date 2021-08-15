package io.github.myifeng.swan.appendix.web;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AppendixController {

    @Value("${swan.upload-folder}")
    private String uploadFolder;

    @PostConstruct
    public void init() throws IOException {
        val path = Paths.get(uploadFolder);
        if (!path.toFile().exists()) {
            Files.createDirectories(path);
        }
    }

    @PostMapping
    public List<String> fileUpload(HttpServletRequest req, HttpServletResponse resp) {
        val mfs = ((StandardMultipartHttpServletRequest) req).getFileMap().values();

        val relativePathStr = req.getParameter("path");
        if (!Strings.isNullOrEmpty(relativePathStr)) {
            val relativePath = Paths.get(uploadFolder, relativePathStr);
            if (!relativePath.toFile().exists()) {
                try {
                    Files.createDirectories(relativePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("createDirectories " + relativePathStr + " fail");
                }
            }
        }
        val filePaths = mfs.stream().map(file -> {
            try {
                String ext = com.google.common.io.Files.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
                if (ext.contains("?")) { // 针对手机上传图库文件，名称结尾含有 ？
                    val idx = ext.indexOf("?");
                    ext = ext.substring(0, idx);
                }
                String outputFilePath;
                if (Strings.isNullOrEmpty(relativePathStr)) {
                    outputFilePath = UUID.randomUUID() + "." + ext;
                } else {
                    outputFilePath = relativePathStr + "/" + UUID.randomUUID() + "." + ext;
                }
                val path = Paths.get(uploadFolder, outputFilePath);

                val in = file.getInputStream();
                val out = Files.newOutputStream(path);
                byte[] tempbytes = new byte[1024];
                int byteread = 0;
                while ((byteread = in.read(tempbytes)) != -1) {
                    out.write(tempbytes, 0, byteread);
                }
                in.close();
                out.close();

                return outputFilePath;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        log.debug("上传文件路径 " + filePaths);

        return filePaths;
    }

    @GetMapping("/**")
    public ResponseEntity<InputStreamResource> getFile(HttpServletRequest request) throws IOException {
        val relativePath = request.getRequestURI();
        val relativePathStr = relativePath.substring(1);
        val res = new FileUrlResource(Paths.get(uploadFolder, relativePathStr).toString());
        return ResponseEntity
                .ok()
                .contentLength(res.getFile().length())
                .contentType(new PathExtensionContentNegotiationStrategy().getMediaTypeForResource(res) == null ? MediaType.APPLICATION_OCTET_STREAM  : new PathExtensionContentNegotiationStrategy().getMediaTypeForResource(res) )
                .body(new InputStreamResource(res.getInputStream()));
    }

}
