#!/bin/bash

#https://alphacephei.com/vosk/models

MODEL_URL="https://alphacephei.com/vosk/models/vosk-model-small-es-0.42.zip"
MODEL_ZIP="vosk-model-small-es-0.42.zip"
MODEL_DIR="vosk-model-small-es-0.42"

mkdir -p "models"
cd "models"

if [ ! -f "$MODEL_ZIP" ]; then
  echo "Downloading model..."
  curl -O "$MODEL_URL"
fi

if [ ! -d "$MODEL_DIR" ]; then
  echo "Unzipping model..."
  unzip "$MODEL_ZIP"
fi

cd ..
export VOSK_MODEL_PATH="$(pwd)/models/$MODEL_DIR"

echo "Model is available at: $VOSK_MODEL_PATH"
echo "To make this permanent, add the following line to your ~/.bashrc or ~/.zshrc:"
echo "export VOSK_MODEL_PATH=\"$VOSK_MODEL_PATH\""

#chmod +x setup-vosk.sh
#./setup-vosk.sh