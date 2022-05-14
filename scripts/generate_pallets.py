from os import listdir
from os.path import join
import re

source_material = "oak"
target_materials = ["jungle", "acacia", "spruce", "dark_oak", "warped", "birch", "crimson"]

assets_path = "src/main/resources/assets/realisticstorage/"
data_path = "src/main/resources/data/realisticstorage/"
folders_to_scan = [
    join(assets_path, "blockstates/"),
    join(assets_path, "models/block/"),
    join(assets_path, "models/item/"),
    join(data_path, "recipes/")
]

for folder in folders_to_scan:
    for filename in listdir(folder):
        path = join(folder, filename)
        if source_material in filename:
            for material in target_materials:
                new_filename = re.sub(source_material, material, filename)
                new_path = join(folder, new_filename)
                print("Writing", new_path)
                with open(path) as file:
                    content = file.read()
                with open(new_path, "w") as file:
                    file.write(re.sub(source_material, material, content))

