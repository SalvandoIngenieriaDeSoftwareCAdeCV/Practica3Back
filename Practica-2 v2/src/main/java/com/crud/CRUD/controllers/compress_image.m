function compress_image(input_file, quality)
  pkg load image;
  img = imread(input_file);
  imwrite(img, input_file, 'Quality', quality);
end

