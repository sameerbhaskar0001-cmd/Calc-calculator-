import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class CropAvatars {
    public static void main(String[] args) {
        String inputPath = "avatars.png";
        if (args.length > 0) {
            inputPath = args[0];
        }
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.out.println("Error: Input file '" + inputPath + "' does not exist.");
            System.out.println("Please upload your image and name it 'avatars.png' in the root directory.");
            return;
        }

        try {
            BufferedImage img = ImageIO.read(inputFile);
            int W = img.getWidth();
            int H = img.getHeight();
            System.out.println("Loaded image successfully. Dimensions: " + W + "x" + H);

            int rows = 2;
            int cols = 4;
            int cellW = W / cols;
            int cellH = H / rows;

            File outputDir = new File("app/src/main/res/drawable");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            int count = 1;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int left = c * cellW;
                    int top = r * cellH;

                    // Scan cell to find the exact bounding box of the circular gold border
                    int minX = cellW, maxX = 0;
                    int minY = cellH, maxY = 0;
                    boolean found = false;

                    for (int x = 0; x < cellW; x++) {
                        for (int y = 0; y < cellH; y++) {
                            int px = img.getRGB(left + x, top + y);
                            int alpha = (px >> 24) & 0xff;
                            int red = (px >> 16) & 0xff;
                            int green = (px >> 8) & 0xff;
                            int blue = px & 0xff;

                            // Ignore transparent background or white background (with tolerance of 240 out of 255)
                            if (alpha > 50 && (red < 240 || green < 240 || blue < 240)) {
                                if (x < minX) minX = x;
                                if (x > maxX) maxX = x;
                                if (y < minY) minY = y;
                                if (y > maxY) maxY = y;
                                found = true;
                            }
                        }
                    }

                    if (!found) {
                        System.out.println("Warning: No avatar found in Row " + (r + 1) + ", Col " + (c + 1));
                        continue;
                    }

                    // Calculate width, height, and center coordinates
                    int width = maxX - minX;
                    int height = maxY - minY;
                    int size = Math.max(width, height);

                    double cx = minX + width / 2.0;
                    double cy = minY + height / 2.0;
                    
                    // Add 2 pixels of margin to make sure we don't clip the outer edge of the gold circle
                    double radius = size / 2.0 + 2.0; 
                    int outSize = (int)(radius * 2);

                    // Create the transparent cropped image
                    BufferedImage dest = new BufferedImage(outSize, outSize, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = dest.createGraphics();
                    
                    // Apply high-quality antialiasing settings for perfect round edges
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // Set alpha transparency
                    g2.setComposite(AlphaComposite.Clear);
                    g2.fillRect(0, 0, outSize, outSize);
                    g2.setComposite(AlphaComposite.Src);

                    // Create circular clip path
                    g2.setClip(new java.awt.geom.Ellipse2D.Double(0, 0, outSize, outSize));

                    // Coordinates to grab from the original image
                    int sourceX1 = (int)(left + cx - radius);
                    int sourceY1 = (int)(top + cy - radius);
                    int sourceX2 = (int)(left + cx + radius);
                    int sourceY2 = (int)(top + cy + radius);

                    g2.drawImage(img, 
                                 0, 0, outSize, outSize, 
                                 sourceX1, sourceY1, sourceX2, sourceY2, 
                                 null);
                    
                    g2.dispose();

                    // Save inside resource drawables
                    File outFile = new File(outputDir, "ic_avatar_" + count + ".png");
                    ImageIO.write(dest, "PNG", outFile);
                    System.out.println("Processed Avatar " + count + " -> " + outFile.getPath() + " (" + outSize + "x" + outSize + ")");
                    count++;
                }
            }
            System.out.println("\nSuccess! Perfectly cropped and saved " + (count - 1) + " transparent circular avatars.");
        } catch (IOException e) {
            System.out.println("Error reading/writing image: " + e.getMessage());
        }
    }
}
