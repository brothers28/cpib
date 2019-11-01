# generates an sml file for the grammar checker Fix & Foxi from a grammar input file
#
# only supports python 3.x
#
# usage:
# python Grammar_to_sml.py <file>
#

import sys
import re
from datetime import datetime

# read file to string
with open(sys.argv[1], 'r') as file:
	string = file.read()

# ===============
# analyse grammar
# ===============
productions = []
nt = set();			# non terminals
t = set();			# terminals
start = None		# start symbol

# remove comments from string
string = re.sub(r'//[^\n]*(?=\n)', '', string)

def substitute_repeat(match):
	name = 'rep_' +  re.sub(r'[\{\}\[\]\| ]', '', match.group(0))
	if name not in nt:
		nt.add(name)
		productions.append((name, [element + [name] for element in process_expr(match.group(1))] + [[]]))
	return name

def substitute_optional(match):
	name = 'opt_' +  re.sub(r'[\{\}\[\]\| ]', '', match.group(0))
	if name not in nt:
		nt.add(name)
		productions.append((name, process_expr(match.group(1)) + [[]]))
	return name

def process_expr(str):
	# repeat { }
	str = re.sub(r'\{(.*?)\}', substitute_repeat, str)

	# optional []
	str = re.sub(r'\[(.*?)\]', substitute_optional, str)

	# or |
	list = [part.split() for part in re.split(r'\s*\|\s*', str)]

	for i in list:
		t.update(i)

	return list

for prod in re.finditer(r'(?P<lhs>\w+)\s*::=\s*(?P<rhs>.*?)\n(?!\s*\|)', string, re.DOTALL):
	lhs = prod.group('lhs')
	rhs = prod.group('rhs')
	#print(f'{lhs} ::= {rhs}')

	productions.append((lhs, process_expr(rhs)))

	if start is None:
		start = lhs
	nt.add(lhs)

t = t - nt

# ====================
# print grammar
# ====================
def print_for_human(t, nt, productions, start):
	"""print the grammar in a human readable form"""
	print(f'start symbol: {start}')
	print()

	print('terminals:')
	for i in sorted(t):
		print(f'    {i}')
	print()

	print('non-terminals:')
	for i in sorted(nt):
		print(f'    {i}')
	print()

	for prod in productions:
		(lhs, rhs) = prod;
		iter_rhs = iter(rhs)
		print(f'{lhs} ::=')
		for i in iter_rhs:
			print(f'    {("[]" if len(i) == 0 else " ".join(i))}')
		print()

def print_sml(t, nt, productions, start):
	# print comment
	print('(*\nfile:\t{}\ntime:\t{}\n*)\n'.format(sys.argv[1], datetime.now()))

	# print terminal and non-terminal symbols
	def print_symbols(name, symbols):
		print(f'datatype {name}')
		iter_symbols = iter(sorted(symbols))
		print(f'  = {next(iter_symbols)}')
		for i in iter_symbols:
			print(f'  | {i}')
		print()
		print(f'val string_of_{name} =')
		iter_symbols = iter(sorted(symbols))
		print('  fn {0} => "{0}"'.format(next(iter_symbols)))
		for i in iter_symbols:
			print('   | {0} => "{0}"'.format(i))
		print()

	print_symbols('term', t)
	print_symbols('nonterm', nt)

	# print some initializations
	print("""
val string_of_gramsym = (string_of_term, string_of_nonterm)

local
  open FixFoxi.FixFoxiCore
in
""")

	# print productions
	print("""
val productions =
[""")

	def prod_to_string(prod):
		(lhs, rhs) = prod;
		str  = f'  ({lhs}, [\n'
		str += ',\n'.join([f'    [{(", ".join([("T " if j in t else "N ") + j for j in i]))}]' for i in rhs])
		str += '])'
		return str

	print(',\n'.join([prod_to_string(prod) for prod in productions]))

	print(']')
	print()

	# print start symbol
	print(f'val S = {start}')

	# print end
	print("""
val result = fix_foxi productions S string_of_gramsym

end (* local *)
""")

# print_for_human(t, nt, productions, start)
print_sml(t, nt, productions, start)
