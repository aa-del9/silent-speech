import cv2
import os
import numpy as np

def resize_images(input_folder, output_folder, width,height):
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    count =0
    for filename in os.listdir(input_folder):
        #print(filename)
        if filename.endswith(('.jpg', '.jpeg', '.png')):
            image_path = os.path.join(input_folder, filename)
        # print(image_path)
            img = cv2.imread(image_path)
            resized_img = cv2.resize(img, (width,height))
            output_path = os.path.join(output_folder, filename)
            print(output_path)
            cv2.imwrite(output_path, resized_img)


# Example usage:
folder = "/media/aadel/Stuff/Projects/DSA/sign-language-interpretor/model/digits"
names = os.listdir(folder)
print(names)
# input_folder = "/media/aadel/Stuff/Projects/DSA/sign-language-interpretor/model/SigNN Character Database/A"
# output_folder = "/media/aadel/Stuff/Projects/DSA/sign-language-interpretor/resized/A"
width=200
height=200 # Set your desired size
for name in names:
    input_folder = "/media/aadel/Stuff/Projects/DSA/sign-language-interpretor/model/digits/"+name+"/Input Images - Sign "+name
    output_folder = "/media/aadel/Stuff/Projects/DSA/sign-language-interpretor/model/digits_resized/"+name
    resize_images(input_folder, output_folder, width,height)
