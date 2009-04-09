#!/usr/local/bin/python
import re


text = "# Petrel Well Tops\n"\
"# Lines starting with # are comments\n"\
"VERSION 1\n"\
"BEGIN HEADER\n"\
"X\n"\
"Y\n"\
"Z\n"\
"MD\n"\
"T\n"\
"# Valid types: HORIZON, FAULT, OTHER\n"\
"Type\n"\
"Surface Name\n"\
"Well Name\n"\
"Symbol\n"\
"Pick Name\n"\
"Interpreter\n"\
"Dip Angle\n"\
"Dip Azimuth\n"\
"END HEADER\n"\
"457920.31 6784738.68 -1783.84 -1701.53 HORIZON \"Base Cretaceous\" \"B4\" Unknown 2068.47 \"\"   \"\"   -999   -999   \n"\
"457746.94 6787092.61 -1886.76 -999     HORIZON \"Base Cretaceous\" \"B8\" Unknown 1886.76 \"\"   \"\"   -999   -999   \n"\
"456719.08 6785550.13 -1836.19 -1740.00 HORIZON \"Base Cretaceous\" \"B9\" Unknown 1836.97 \"\"   \"\"   -999   -999   \n"\
"453078.12 6786788.35 -1996.92 -1894.14 HORIZON \"Base Cretaceous\" \"C1\" Unknown 2003.34 \"\"   \"\"   -999   -999   \n"\
"454686.86 6787607.12 -1977.81 -1878.58 HORIZON \"Base Cretaceous\" \"C2\" Unknown 1998.97 \"\"   \"\"   -999   -999   \n"\
"456306.69 6788724.75 -1989.68 -1883.87 HORIZON \"Base Cretaceous\" \"C3\" Unknown 2004.29 \"\"   \"\"   88.86  -54.66 \n"\
"454693.38 6786210.63 -1965.23 -1859.14 HORIZON \"Base Cretaceous\" \"C4\" Unknown 1980.43 \"\"   \"\"   88.23  -70.43 \n"\

# String to import
#s = '12.0 13.0 14.0 HORIZON "Top Tarbert" "Base Cretaciouse" 12.3'
lines = text.splitlines()
rx = re.compile('([\-0-9.]+|\w+|"[a-zA-Z ]+")')
ms = rx.findall(lines[21])
for m in ms:
	print(m.strip('""'))

#print(s)
