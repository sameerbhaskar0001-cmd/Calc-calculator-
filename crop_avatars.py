import os
import sys
import numpy as np
from PIL import Image, ImageDraw

def crop_avatars(image_path, output_dir="app/src/main/res/drawable"):
    if not os.path.exists(image_path):
        print(f"Error: Input image file '{image_path}' not found.")
        print("Please upload the file to the workspace first!")
        return

    # Load image and convert to RGBA
    try:
        img = Image.open(image_path).convert("RGBA")
    except Exception as e:
        print(f"Error opening image: {e}")
        return

    os.makedirs(output_dir, exist_ok=True)
    W, H = img.size
    print(f"Original Image Size: {W}x{H}")

    # We have 2 rows and 4 columns of avatars
    rows = 2
    cols = 4
    cell_w = W // cols
    cell_h = H // rows

    avatar_count = 0

    for r in range(rows):
        for c in range(cols):
            avatar_count += 1
            # Step 1: Crop the rough cell
            left = c * cell_w
            top = r * cell_h
            right = (c + 1) * cell_w
            bottom = (r + 1) * cell_h
            cell = img.crop((left, top, right, bottom))

            # Step 2: Find bounding box of non-white pixels
            # Convert to numpy array to find non-white pixels quickly
            data = np.array(cell)
            # Threshold to identify non-white/non-transparent background
            # Background is white (255, 255, 255)
            r_channel = data[:, :, 0]
            g_channel = data[:, :, 1]
            b_channel = data[:, :, 2]
            alpha_channel = data[:, :, 3]

            # Pixels that are not white (with a small tolerance) and are not transparent
            non_white_mask = (r_channel < 240) | (g_channel < 240) | (b_channel < 240)
            if alpha_channel.any():
                non_white_mask = non_white_mask & (alpha_channel > 50)

            # Find coordinates of non-white pixels
            y_indices, x_indices = np.where(non_white_mask)

            if len(x_indices) == 0 or len(y_indices) == 0:
                print(f"Warning: Could not find avatar in row {r+1}, col {c+1}")
                continue

            min_x, max_x = x_indices.min(), x_indices.max()
            min_y, max_y = y_indices.min(), y_indices.max()

            # The exact bounding box of the circular avatar (with gold border)
            width = max_x - min_x
            height = max_y - min_y
            size = max(width, height)

            # Center of the detected circle
            cx = min_x + width / 2
            cy = min_y + height / 2
            radius = size / 2

            # Let's add a tiny margin of 2 pixels to make sure the outer edge of the gold border isn't clipped
            radius += 2
            size = int(radius * 2)

            # Crop the circle area with precise coordinates
            crop_box = (
                int(cx - radius),
                int(cy - radius),
                int(cx + radius),
                int(cy + radius)
            )
            cropped_avatar = cell.crop(crop_box)

            # Create circular transparent mask
            mask = Image.new("L", (size, size), 0)
            draw = ImageDraw.Draw(mask)
            draw.ellipse((0, 0, size, size), fill=255)

            # Apply circular mask
            output_avatar = Image.new("RGBA", (size, size), (0, 0, 0, 0))
            output_avatar.paste(cropped_avatar, (0, 0), mask=mask)

            # Save the cropped avatar
            output_filename = f"ic_avatar_{avatar_count}.png"
            output_filepath = os.path.join(output_dir, output_filename)
            output_avatar.save(output_filepath, "PNG")
            print(f"Successfully saved Avatar {avatar_count} -> {output_filepath} (Size: {size}x{size})")

    print("\nAll 8 avatars cropped and saved successfully in the drawables directory!")

if __name__ == "__main__":
    img_file = "avatars.png"
    if len(sys.argv) > 1:
        img_file = sys.argv[1]
    crop_avatars(img_file)
