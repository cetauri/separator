import os
source = "../2016"

matches = []
for root, dirnames, filenames in os.walk(source):

    for filename in filenames:
        matches.append(os.path.join(root, filename))
# return matches

print len(matches)

    # for root, dirnames, filenames in os.walk(source):
    #     for filename in filenames:
    #         if filename.endswith(('.mov', '.MOV', '.avi', '.mpg')):
    #             matches.append(os.path.join(root, filename))
    # return matches