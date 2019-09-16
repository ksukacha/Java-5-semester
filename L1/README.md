# Лабораторная №1

## Задание
  - Имея нефиксированный файл переносов (например, «аг-рес-сор», «се-год-ня» и т.д.) (т.е. он задается в настройках и может быть изменен пользователем), проверять позиции переносов и оценивать их корректность, при ошибках предлагать исправить. 
## Особенности
- Обработка закрытия/сохранения/открытия с использованием JFileChooser и JOptionPane
- ###### Функции для обработки текста
  - ```public static boolean wordCorrectDivided(String word, Map<String, String> dictionary)```проверяет правильности постановки переноса в слове, если оно содержится в словаре dictionary
  - ```public static ChangeMessage getWordChangeMessage(String word, Map<String, String> dictionary)``` возвращает объект ```ChangeMessage(String message, String correctWordDivision, boolean hasDash )```; ищет ближайшую позицию правильного переноса в слове word
