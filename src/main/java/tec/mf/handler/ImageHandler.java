package tec.mf.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import tec.mf.handler.io.ImageHandlerResponseWriter;
import tec.mf.handler.io.ImageRequest;
import tec.mf.handler.io.InputEventParser;
import tec.mf.handler.service.ImageService;

import java.io.*;


public class ImageHandler implements RequestStreamHandler {

    private static final AppConfig APP_CONFIG;
    private final AppConfig appConfig;

    static {
        APP_CONFIG = AppConfig.getInstance();
    }

    public ImageHandler() {
        this(APP_CONFIG);
    }

    public ImageHandler(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        LambdaLogger logger = context.getLogger();

        logger.log("Inside Image Handler ");
        ImageRequest imageRequest = this.inputEventParser().processInputEvent(inputStream, logger);
        //TODO: pass ImageRequest as argument instead of filename, width and height
        InputStream imageResized = this.imageService().getImageFrom(imageRequest);
        this.imageHandlerResponseWriter().writeResponse(imageResized, outputStream, imageRequest);


//        //TODO: move this
//        String imageResizedBase64 = "";
//        try{
//            byte imageData[] = new byte[imageResized.available()];
//            imageResized.read(imageData);
//            imageResizedBase64 = Base64.getEncoder().encodeToString(imageData);
//        } catch (Exception e) {
//            //TODO: change this
//            e.printStackTrace();
//        }

// -------------------------------

//        ObjectMapper mapper = new ObjectMapper();
//        ImageResizeRequest request = mapper.readValue("{\"imageUrl\":\"https://github.com/some/image.jpg\",\"height\":400, \"width\":500}", ImageResizeRequest.class);
//        ImageResizeRequest request = mapper.readValue(inputStream, ImageResizeRequest.class);

//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//        objectOutputStream.writeObject(request);
//        objectOutputStream.close();


//        ImageRequest imageRequest = imageRequestParser.processEvent(inputStream, logger);
//        File file = new File("51ca22dd0cf293ac67bb394a-295xh.jpg");
//        BufferedImage bufferedImage = ImageIO.read(file);
//        String imageResizedBase64 = "";
//        try(InputStream imageResized = resizeImage(bufferedImage, context)){
//            byte imageData[] = new byte[imageResized.available()];
//            imageResized.read(imageData);
//            imageResizedBase64 = Base64.getEncoder().encodeToString(imageData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        InputStream imageStream = s3Dao.getImage("51ca22dd0cf293ac67bb394a-295xh.jpg");
//        BufferedImage bufferedImage = ImageIO.read(imageStream);


// -------------------------------------------

//        JSONObject responseJson = new JSONObject();
//        JSONObject responseBody = new JSONObject();
//        responseBody.put("filename", imageRequest.getFilename());
//        responseBody.put("width", imageRequest.getWidth());
//        responseBody.put("height", imageRequest.getHeight());
//
//        JSONObject headerJson = new JSONObject();
//        headerJson.put("Content-Type", "image/jpeg");
//        headerJson.put("x-resize-header", "my custom header value");
//
//        responseJson.put("isBase64Encoded", true);
//        responseJson.put("statusCode", 200);
//        responseJson.put("headers", headerJson);
//        responseJson.put("body", imageResizedBase64);
//
//        logger.log(responseJson.toJSONString());
//        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
//        writer.write(responseJson.toJSONString());
//        writer.close();
    }

    private InputEventParser inputEventParser() {
        return this.appConfig.getInputEventParser();
    }

    private ImageService imageService() {
        return this.appConfig.getImageService();
    }

    private ImageHandlerResponseWriter imageHandlerResponseWriter() {
        return this.appConfig.getImageHandlerResponseWriter();
    }

//    private static String getStringFromInputStream(InputStream is) {
//
//        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();
//
//        String line;
//        try {
//
//            br = new BufferedReader(new InputStreamReader(is));
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return sb.toString();
//
//    }
//
//    private InputStream resizeImage(BufferedImage image, Context cntxt)
//    {
//        try {
//            BufferedImage img = Scalr.resize(image, Scalr.Method.BALANCED, Scalr.Mode.AUTOMATIC, 100, 100, Scalr.OP_ANTIALIAS);
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            ImageIO.write(img, "jpeg", os);
//            InputStream is = new ByteArrayInputStream(os.toByteArray());
//            return is;
//        }
//        catch (IOException ex) {
//            cntxt.getLogger().log("Image Resizing failed : " + " " + ex.getMessage());
//            return null;
//        }
//    }
//
//    private static String base64Encoder() {
//
//        File file = new File("51ca22dd0cf293ac67bb394a-295xh.jpg");
//        try(FileInputStream imageInFile = new FileInputStream(file)) {
//            byte imageData[] = new byte[imageInFile.available()];
//            imageInFile.read(imageData);
//            return Base64.getEncoder().encodeToString(imageData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }
}
