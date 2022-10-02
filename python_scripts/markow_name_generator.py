# This is a sample Python script.
import pandas as pd
import numpy
import random
import string
import json
import sys


def validate_input_df(df):
    return 'name' in df.columns and 'race' in df.columns


def read_data_frame(file):
    df = pd.read_csv(file)

    if not validate_input_df(df):
        raise ValueError('Input must have columns: name and race.')

    return df


def prepare_date(df):
    clear_df = df.drop(columns=['birth', 'death', 'gender', 'hair', 'height', 'realm', 'spouse']).dropna()
    clear_df.replace(to_replace="Dragons", value="Dragon", inplace=True)
    clear_df.replace(to_replace="Urulóki", value="Dragon", inplace=True)
    clear_df.replace(to_replace="Orcs", value="Orc", inplace=True)
    clear_df.replace(to_replace="Elves", value="Elf", inplace=True)
    clear_df.replace(to_replace="Elves,Maiar", value="Elf", inplace=True)
    clear_df.replace(to_replace="Elves,Noldor", value="Elf", inplace=True)
    clear_df.replace(to_replace="Half-elven,Men", value="Elf", inplace=True)
    clear_df.replace(to_replace="Hobbits", value="Hobbit", inplace=True)
    clear_df.replace(to_replace="Dwarves", value="Dwarf", inplace=True)
    clear_df.replace(to_replace="Dwarven", value="Dwarf", inplace=True)
    clear_df.replace(to_replace="Ents", value="Ent", inplace=True)
    clear_df.replace(to_replace="Ents,Onodrim", value="Ent", inplace=True)
    clear_df.replace(to_replace="Men,Wraith", value="Men", inplace=True)
    clear_df.replace(to_replace="Men,Undead", value="Men", inplace=True)
    clear_df.replace(to_replace="Men,Rohirrim", value="Men", inplace=True)
    clear_df.replace(to_replace="Drúedain", value="Men", inplace=True)
    clear_df.replace(to_replace="Uruk-hai", value="Orc", inplace=True)
    clear_df.replace(to_replace="Orc,Goblin", value="Orc", inplace=True)
    clear_df.replace(to_replace="Goblin,Orc", value="Orc", inplace=True)
    clear_df.replace(to_replace="Black Uruk", value="Orc", inplace=True)
    clear_df.replace(to_replace="Uruk-hai,Orc", value="Orc", inplace=True)
    clear_df.replace(to_replace="Skin-changer", value="Beorning", inplace=True)
    clear_df.replace(to_replace="Men,Skin-changer", value="Beorning", inplace=True)
    clear_df.replace(to_replace="Maiar,Balrog", value="Maiar", inplace=True)
    clear_df.replace(to_replace="Balrog", value="Maiar", inplace=True)
    clear_df.replace(to_replace="Maiar,Balrogs", value="Maiar", inplace=True)
    clear_df.replace(to_replace="Ainur,Maiar", value="Maiar", inplace=True)
    clear_df.drop(clear_df.loc[clear_df['race'].isin(
        ['Great Spiders', 'Great Eagles', 'Werewolves', 'Stone-trolls', 'Eagle', 'Wolfhound', 'Eagles', 'Horse', 'God',
         'Raven', 'Vampire'])].index, inplace=True)
    return clear_df['race'].unique().tolist(), clear_df


def generate_markow_level_dictionary(content, level):
    dictionary = dict()
    previous_letter = ""
    for i in range(level):
        previous_letter += content[0][i]
    count = 0
    for name in content:
        for letter in name:
            if count == level - 1:
                if previous_letter not in dictionary:
                    dictionary[previous_letter] = dict()
                    dictionary[previous_letter][letter] = 1
                else:
                    if letter not in dictionary[previous_letter]:
                        dictionary[previous_letter][letter] = 1
                    else:
                        dictionary[previous_letter][letter] += 1
                previous_letter += letter
                previous_letter = previous_letter[-level:]
            else:
                count += 1
    for previous in dictionary:
        length = 0
        for letter in dictionary[previous]:
            length += dictionary[previous][letter]
        for letter in dictionary[previous]:
            dictionary[previous][letter] = dictionary[previous].get(letter) / length
    # print(dictionary)
    return dictionary


def get_next_letter(dictionary, pattern_word):
    if pattern_word in dictionary:
        next_word = numpy.random.choice(list(dictionary[pattern_word].keys()), 1,
                                        p=list(dictionary[pattern_word].values()))
    else:
        word = numpy.random.choice(list(dictionary.keys()), 1)
        next_word = numpy.random.choice(list(dictionary[word].keys()), 1, p=list(dictionary[word].values()))
    return next_word[0]


def markow_string(dictionary, markow_level, signs_number):
    start_word = numpy.random.choice(list(dictionary.keys()))
    while start_word[0] in '-\'\" ' or start_word[1] in '-\'\" ':
        start_word = numpy.random.choice(list(dictionary.keys()))

    last_string = start_word[-markow_level:]
    message = start_word

    for i in range(signs_number):
        next_sign = get_next_letter(dictionary, last_string)
        message = message + next_sign
        last_string += next_sign
        last_string = last_string[-markow_level:]
    return string.capwords(message)


def generate_start_string(i):
    signs = "qwertyuiopasdfghjklzxcvbnm QWERTYUIOPASDFGHJKLZXCVBNM"
    message = ""
    for x in range(i):
        message += signs[random.randint(0, len(signs) - 1)]
    return message


def save_as_json(dictionary, file_name):
    json_object = json.dumps(dictionary, indent=1)
    with open(file_name, mode='w') as out:
        out.write(json_object)


def example_names(dictionary, level):
    for i in range(100):
        print(markow_string(dictionary, level, random.randint(3, 16)))


def read_args(args):
    if len(args) < 3:
        raise AssertionError("Script require: markow level, input file name.")
    return args[1], int(args[2])


def main(args):
    input_file_name, level = read_args(args)

    raw_data = read_data_frame(open(input_file_name))

    races_list, cleared_df = prepare_date(raw_data)

    markow_dictionary = generate_markow_level_dictionary(cleared_df['name'], level)

    example_names(markow_dictionary, level)

    save_as_json(markow_dictionary, 'markow_dict_level_' + str(level) + '.json')


if __name__ == '__main__':
    main(sys.argv)
