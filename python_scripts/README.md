### Character name generator base on markow chain

The script creates a json file that contains the probabilities of the letters after the string long to input parame (markow level).

> Input:
> - input_file_name -> file name to data
> - markow_level -> number of letter in key strings

Example command: <code>python3 markow_name_generator.py lotr_characters.csv 3</code>

> Output:
> - 100 example names printed in console
> - file <code>markow_dict_level_[markow_level].json</code> -> dictionary with probabilities

<code>lotr_characters.csv</code> source: <link>https://www.kaggle.com/datasets/paultimothymooney/lord-of-the-rings-data?resource=download </link>