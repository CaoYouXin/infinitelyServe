package tech.caols.infinitely.server.handlers;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.afd.AFD;
import tech.caols.infinitely.afd.AFDEventHandler;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(UploadHandler.class);

    private final String uploadRoot;

    public UploadHandler(String uploadRoot) {
        this.uploadRoot = uploadRoot;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!HttpUtils.isPost(httpRequest)) {
            throw new MethodNotSupportedException(HttpUtils.getMethod(httpRequest) + " method not supported");
        }

        HttpEntity entity = HttpUtils.getBody(httpRequest);
        if (null == entity) {
            throw new RuntimeException("empty body for upload, ridiculous");
        }

        Header contentType = httpRequest.getHeaders("Content-Type")[0];
        int indexOf = contentType.getValue().indexOf(';');
        String type = contentType.getValue().substring(0, indexOf);

        if (!"multipart/form-data".equals(type)) {
            throw new RuntimeException("wrong content type, ridiculous");
        }

        String boundary = contentType.getValue().substring(indexOf + "; boundary=".length());
        logger.info(Arrays.toString(httpRequest.getHeaders("Content-Length")));

        byte[] boundaryBytes = ("\r\n--" + boundary).getBytes(Consts.ASCII);
        logger.info("boundary : " + Arrays.toString(boundaryBytes));
        final List<FileItem> items = new ArrayList<>();
        AFD afd = new AFD(new AFDEventHandler() {

            @Override
            public void onHeaderName(byte[] headerNameBytes) {
                String name = new String(headerNameBytes);
                logger.info("header name : " + name);

                FileItem fileItem = items.get(items.size() - 1);
                fileItem.addHeader(new FileItem.FileItemHeader());
                fileItem.getLastHeader().setName(name);
            }

            @Override
            public File onHeaderEnd() {
                FileItem fileItem = items.get(items.size() - 1);
                if (fileItem.isFile()) {
                    return fileItem.getFile();
                }
                return null;
            }

            @Override
            public void onHeaderValue(byte[] headerValueBytes) {
                String value = new String(headerValueBytes);
                logger.info("header value : " + value);

                FileItem fileItem = items.get(items.size() - 1);
                String fileName = fileItem.getLastHeader().setValue(value);
                if (null != fileName) {
                    fileItem.setFile(true);
                    fileItem.setFile(new File(uploadRoot, fileName));
                }
            }

            @Override
            public void onPartData(byte[] partDataBytes) {
                String partValue = new String(partDataBytes, Consts.UTF_8);
                logger.info("part value raw : " + Arrays.toString(partDataBytes));
                logger.info("part value : " + partValue);

                FileItem fileItem = items.get(items.size() - 1);
                fileItem.setPartValue(partValue);
            }

            @Override
            public void onNewPart() {
                items.add(new FileItem());
            }

        }, boundaryBytes);

        InputStream content = entity.getContent();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(content);
        byte[] buffer = new byte[8 * 1024];
        int read;
        while (-1 != (read = bufferedInputStream.read(buffer))) {
            for (int i = 0; i < read; i++) {
                afd.processRead(buffer[i]);
//                System.out.print(buffer[i]);
//                System.out.print(" ");
            }
        }

        logger.info(Arrays.toString(items.toArray()));
        HttpUtils.response(httpResponse, new JsonRes(Constants.CODE_VALID));
    }


}
