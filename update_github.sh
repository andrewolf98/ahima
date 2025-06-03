#!/bin/bash

# Настройки Git (установите один раз, если нужно)
git config user.name "andrewolf98"
git config user.email "your_email@example.com"

# Обновим фиксированный Python-скрипт
python3 fix.py

# Добавим все изменения
git add .

# Сделаем коммит
git commit -m "Автообновление проекта через fix.py и генерацию UI"

# Отправим в GitHub
git push origin main

echo "Проект успешно обновлён и отправлен на GitHub."
