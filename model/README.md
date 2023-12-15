# Sign Language Interpretor Model

## Usage

**Preparation of Dataset**

Prepare dataset with subfolders, each with name of each class, and having relevant images. To resize images, use the `resize.py` file, giving appropriate `width` and `height`.

**Training**

To train your own model, in the `gesture_recognizer.ipynb` file, provide dataset path in `dataset_path`.

```python
dataset_path = "path/to/your/dataset"
```

And give the exported model folder name in `gesture_recognizer.HParams()`

```python
hparams = gesture_recognizer.HParams(export_dir="exportedModel")
```

## Tech Stack

**Model:** Mediapipe![Logo](https://assets.codepen.io/5409376/internal/avatars/users/default.png?fit=crop&format=auto&height=512&version=1607020963&width=512)

## Demo

To run the demo on webcam on pc, run the `new.ipynb` file. It is important to give the model path to be used for recognition.

```python
options = GestureRecognizerOptions(
    base_options=BaseOptions(model_asset_path='path/to/your/.task file'),
    running_mode=VisionRunningMode.LIVE_STREAM,result_callback=print_result
    )
```
