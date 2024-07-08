# Artifical Neural Network

ANN for Mnist training and classification.

## Compile

```console
javac *.java
```
## Usage

### Training:

```console
java -cp . Digits training-images.txt training-labels.txt validation-images.txt > result.txt
```

### Check classifications

```console
java -cp . MnistLabelsCheck result.txt validation-labels.txt
```
