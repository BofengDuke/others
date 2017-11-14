#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 实基于平衡的Feisted网络的分组加密实现DES加密解密 

Feisted网络的分组加密实现:
	注: 下面讲述,规定大写字母后面的小写字母,数字均为下标,括号中有小写字母的也为下表,如 Ri,R(i-1)
		括号中有 sup 的表示上标,如 IP(sup -1)
	Feistel型分组加密加密过程如下:
		1) 将明文 X 一分为二.设 X = L0R0, L0是左边的 m 比特,R0 是右边的 m 比特.
		2) 对于 1 <= i <= r 计算 Li,Ri
				Li = R(i-1)
				Ri = L(i-1) 异或 F( R(i-1) , Ki )
			其中:
			- Li 和 Ri 的长度都是 m 比特, 
			- F 是一个加密函数,称为圈函数.
			- Ki 是由密钥 k 产生的长度为 t 比特的子密钥;
			- 异或: 是按位模 2 加运算.
		3) 密文为 y = RrLr , r 是圈变换的迭代次数
'''

import sys
import binascii
from config import IP_table,IP_inverse_table,PC_1_table,PC_2_table,S_BOX,P_table

def des_encrypt(plaintext,key='0123456789ABCDEF',detail=None):
	""" DES加密算法步骤:
		1) 获取 64bit 的十六进制明文 X = X1X2X3......X64
		2) 进行初始置换 IP,打乱明文顺序,将置换后的明文假设为 A
		3) 将 A 分为 L0,R0,两组,长度分别为 32 bit
		4) 调用 Feisted 的步骤 2 计算过程, 分别获得 L1,R1
		5) 重复步骤 4 ,持续进行 16 次
		6) 对获得的密文,进行逆初始置换 IP(sup -1)
		7) 返回十六进制密文

		:Parameter
			- X: 输入的长度为64bit的16进制明文
			-key: 64bit 长的十六进制秘钥
			return: 返回一个长度为64bit的16进制密文
	"""
	bin_x = hex2bin(plaintext)
	text = _IP_switch(bin_x)
	L0 = text[0:len(text)//2]
	R0 = text[len(text)//2:]
	keys = _get_key(key)

	left = L0
	right = R0
	for i in range(16):
		left_new = right
		func_value = _encrypt_func(right,keys[i])
		right_new = XOR(left,func_value)
		if detail:
			print('{} {} {}'.format(i,bin2hex(left_new+right_new),keys[i]))
		left = left_new
		right = right_new
	_ciper = left + right
	ciper = _IP_inverse_switch(_ciper)
	ciper = bin2hex(ciper)
	return ciper

def des_decrypt(ciper,key='0123456789ABCDEF',detail=None):
	""" DES的解密过程与加密过程使用同一算法,只不过在16次迭代中使用
		子秘钥的次序正好相反.
	:Parameter:
		- ciper: 64bit长的16进制密文
		- key: 解密秘钥,64bit长的十六进制值
	"""
	bin_x = hex2bin(ciper)
	text = _IP_switch(bin_x)
	left = text[0:len(text)//2]
	right = text[len(text)//2:]
	keys = _get_key(key)

	for i in range(16):
		right_new = left
		func_value = _encrypt_func(left,keys[len(keys)-i-1])
		left_new = XOR(right,func_value)
		if detail:
			print('{} {} {}'.format(i,bin2hex(left_new+right_new),len(keys)-i-1),keys[len(keys)-i-1])
		left = left_new
		right = right_new
	_plaintext = left + right
	plaintext = _IP_inverse_switch(_plaintext)	
	plaintext = bin2hex(plaintext)
	return plaintext

def _IP_switch(X):
	""" 初始置换 IP
		:Parameter:
			- X: 要置换的 64bit 长度的二进制数
			return: 返回置换后的 64bit 长度的二进制数
	"""
	new = []
	for i in range(len(IP_table)):
		new.append(X[IP_table[i]-1])
	return ''.join(new)

def _IP_inverse_switch(order):
	""" 逆初始置换 IP
		目的是对进行初始置换IP的值,再次进行逆置换
	:Paramter:
		- order: 64bit长的二进制
	"""
	order_new = []
	for i in range(len(IP_inverse_table)):
		value = order[IP_inverse_table[i]-1]
		order_new.append(value)
	return ''.join(order_new)

def _E_box(R):
	""" 输入32bit长的R,经过E盒扩展,获得48bit长的值
		算法过程:
			1) 将原来的32bit长的值,分成8行4列,
			2) 扩展: 每行的第一个值前添加上一行的最后一个值,每行的最后一个值后添加下一行的第一个值
			e.g.  1  2  3  4
			      5  6  7  8
			      9 10 11 12
			    扩展:
			      12  1  2  3  4  5
			      4   5  6  7  8  9
			      8   9 10 11 12  1
			 3) 返回48bit长的序列
	"""
	col_num = 4
	_list = list(R)
	row = []
	matrix = []
	for i in range(len(_list)):
		if i%col_num == 0:
			row.append(_list[i-1])
		row.append(_list[i])
		if (i+1) % col_num == 0:
			row.append(_list[(i+1)%len(_list)])
			matrix.append(''.join(row))
			row=[]
	return ''.join(matrix)

def _S_box(order):
	""" S盒选择压缩,将48bit长的序列经过选择压缩,变为32bit
	:Paramter:
		- order: 48比特长的序列
	步骤:
	1) 将序列等分成8组,
	2) 每组按照对应的S盒子进行选择压缩.
	3) 压缩方法: X=b1b2b3b4b5b6 ,其中b1b8作横坐标,b2b3b4b5作纵坐标,
				所对应的S盒中的值就是新值.
	"""
	g_count = 8
	groups = avg_split(order,g_count)
	value = []
	for i in range(g_count):
		group = groups[i]
		s_box = S_BOX[i]
		x = group[0]+group[-1]
		y = group[1:5]
		_x = int(x,2)
		_y = int(y,2)
		# s_box 是一维列表,所以_x,_y 对应的一维坐标为 _x*ROW + COL
		new = s_box[_x*16+_y]
		value.append(bin(new).lstrip('0b').rjust(4,'0'))
	return ''.join(value)

def _P_box(order):
	""" P盒置换,目的是将S盒中输入的32bit值打乱顺序
	"""
	new_order = []
	for i in range(len(P_table)):
		value = order[P_table[i]-1]
		new_order.append(value)
	return ''.join(new_order)

def avg_split(order,count=1):
	"""	对序列进行等分切片
	:Parameter:
		- order: 要进行分片的序列,
		- count: 分片数量
	"""
	groups = []
	group = []
	for i in range(len(order)):
		group.append(order[i])
		if (i+1) % (len(order)//count) == 0:
			groups.append(''.join(group))
			group = []
	return groups

def _get_key(key):
	""" 获取 Ki, 每次置换,key会根据算法而不同,
		:Parameter
			- key: 64bit 长的十六进制秘钥
		算法过程如下:
		1) 获取64bit的key,
		2) 通过选择置换 PC-1 ,除去秘钥 key 中 8 位的奇偶校验位,并对其余56位打乱顺序
		3) 将经 PC-1 输出的值,等分为 C0 , D0
		4) 将 C0,D0 循环左移,分别获得 C1,D1
		5) 通过选择置换 PC-2,从C1,D1的组合 C1D1中,选出 48 位作为秘钥 key1
		6) 将 C1,D1 作为新的输入,重复 4,5 步骤,持续获得 k2,k3...k16
		7) 返回新秘钥 k 的列表,共16个秘钥
	"""
	key_list = hex2bin(key)
	key_list_56 = []
	for i in range(len(PC_1_table)):
		# 选择置换 PC-1,对 64bit 密钥取56位
		key_list_56.append(key_list[PC_1_table[i]])
	lkey = key_list_56[0:len(key_list_56)//2]
	rkey = key_list_56[len(key_list_56)//2:]
	lkey = ''.join(lkey)
	rkey = ''.join(rkey)
	key_new_list = []

	for i in range(1,17):
		if i in [1,2,9,16]:
			# LS1,LS2,LS9,LS16 只循环左移1位
			lkey = ROL(lkey,count=1)
			rkey = ROL(rkey,count=1)
		else:
			# 其他的,循环左移2次
			lkey= ROL(lkey,count=2)
			rkey= ROL(rkey,count=2)
		key_list_56 = lkey+rkey

		key_new = []
		for i in range(len(PC_2_table)):
			# 将获得的56bit的值经过选择置换 PC-2 ,获得48bit长的新的key
			key_new.append(key_list_56[PC_2_table[i]-1])
		key_new_list.append(''.join(key_new))

	return key_new_list

def key_generator(key):
	"""	根据输入的ascii密钥,生成十六进制,64bit长的密钥,用于DES加密中
	"""
	key = key.strip().encode('utf-8')
	key = binascii.b2a_hex(key)

	space = ' '.encode('utf-8')
	space = binascii.b2a_hex(space)
	if len(key) < 16:
		r = (16-len(key))//2
		for i in range(r):
			key += space
	key = str(key[:16],'utf-8')
	return key

def _encrypt_func(plaintext,key):
	""" 实现加密函数 f( R(i-1),Ki )
		:Parameter:
			- plaintext : 加密的明文,32bit长的二进制序列,在DES中为 R
			- key: 加密秘钥,48bit长的二进制序列
		
		步骤:
		1) 输入 R(i-1),经过E扩展,获得新值 E1
		2) E1 与 key 异或,获得新的48比特值 E2
		3) E2 经过S盒的压缩置换,获得 32bit 的E3
		4) E3 经过置换函数P进行换位处理, 获得结果 ciper
		5) 返回 ciper
	"""
	E1 = _E_box(plaintext)
	output_matrix(E1,col=6,name='E1')
	E2 = XOR(E1,key)
	output_matrix(E1,col=6,name='E2')
	E3 = _S_box(E2)
	output_matrix(E3,col=4,name='E3')
	ciper = _P_box(E3)
	output_matrix(ciper,col=4,name='ciper')

	return ciper

def output_matrix(l,col,name=None,flag=None):
	""" 格式化输出矩阵
	"""
	if flag:
		print('\n### output: {} , len: {}'.format(name,len(l)))
		for i in range(len(l)):
			print('{} '.format(l[i]),end=' ')
			if (i+1) % col == 0:
				print('')

def XOR(a,b):
	""" 实现二进制的异或
	:Parameter:
		- a,b 为传进的两个二进制的字符串
	"""	
	width = len(a) if len(a) >= len(b) else len(b)
	value = int(a,2) ^ int(b,2)
	value = bin(value).lstrip('0b').rjust(width,'0')
	return value

def ROL(value,count=1):
	""" 二进制循环左移n位
	:Parameter:
		- value: 移位的值
		- count: 循环左移的个数
	"""
	new = value.ljust(len(value)+count,'0')
	pre = list(value[0:count])
	new = list(value[count:].ljust(len(value),'0'))
	# 将移位后的值分为两部分,将溢出(从后往前,超过原长度)的部分取出为pre,
	# 用pre的值代替new中后面因为移位而自动补的0
	for i in range(count):
		n = i+1
		new[-n] = pre[-n]
	return ''.join(new)

def hex2bin(hex_x):
	""" 十六进制转为二进制,每个十六进制转为长度为4的二进制
		:Parameter:
			- hex_x: 十六进制的str类型,或者bytes
			- width: 每个十六进制转为的二进制的固定长度,默认为4, None表示不处理
	"""
	bin_str = ''
	for x in hex_x:
			num = bin(int(x,16)).replace('0b','').rjust(4,'0')
			bin_str += num

	return bin_str

def bin2hex(bin_x):
	""" 将二进制字符串转为十六进制
	:Parameter:
		- bin_x: 输入的二进制字符串
	"""
	count,rem = divmod(len(bin_x),4)
	if rem != 0:
		# 填充,为4的倍数
		count += 1
		bin_x.rjust(count*4,'0')

	hex_list = []
	for i in range(count):
		_bin = bin_x[i*4:(i+1)*4]
		_hex = hex(int(_bin,2)).replace('0x','')
		hex_list.append(_hex)
	return ''.join(hex_list)

def encrypt_string(string,key):
	""" DES加密字符串,字符串中可以有中文,字母,数字,符号
	:Parameter:
		- string: 加密的字符串 
		
	:Return: 返回经过DES加密的结果

	步骤:
		1) 将字符串进行utf-8编码,再将编码后的字符串转为十六进制
		2) 将十六进制字符串分为N组,每组长度为64bit
		3) 最后一组不足64bit的,用空格的ascii'0x32'填补,需转为十六进制
		4) 返回加密结果
	"""
	string = string.strip().encode('utf-8')
	string = binascii.b2a_hex(string)
	x,y = divmod(len(string),16)
	x = x+1 if y != 0 else x

	space = ' '.encode('utf-8')
	space = binascii.b2a_hex(space)

	# 分组
	group = []
	for i in range(x):
		_str = string[i*16:(i+1)*16]
		group.append(_str)

	if len(group[-1]) != 16:
		c = (16 - len(group[-1])) // 2
		for i in range(c):
			group[-1] += space

	ciper = []
	for text in group:
		text = str(text,'utf-8')
		c = des_encrypt(text,key = key)
		ciper.append(c)
	return ''.join(ciper)

def decrypt_string(string,key):
	""" 对字符串加密的密文进行解密
	步骤:
		1) 将密文分N组,每组长度为64bit, 不为64bit倍数的,则是非法密文,报错
		2) 对每组进行解密,然后进行组合
		3) 将str类型的十六进制转为bytes类型,去掉最后面的空格
		4) 将bytes类型用a2b_hex()转换成16进制,再进行utf-8解码
	"""
	x,r = divmod(len(string),16)
	if r != 0:
		raise("[Error] 不是正确的DES密文")

	group = []
	for i in range(x):
		group.append(string[i*16:(i+1)*16])

	text = ''
	for c in group:
		text += des_decrypt(c,key)

	text = ''.join(text)
	text = bytes(text,'utf-8')
	text = binascii.a2b_hex(text)
	text = text.decode('utf-8').rstrip()
	return text
		
	


def test_keys():
	# ip = IP_table
	# ip1 = _IP_inverse_switch(ip)
	print('test key')
	key = '0123456789ABCDEF'
	keys = _get_key(key)
	count = 1
	for key in keys:
		print('%2d  %s'%(count,key))
		count += 1

	print('\n')

def test():
	text = 'hello world'
	key1 ='ciper'
	key = key_generator(key1)
	print(key,len(key))
	print('---------原文')
	print("|%s|"%text)
	print('---------加密')
	ciper = encrypt_string(text,key)
	print("|%s |"%ciper)
	x,y = divmod(len(ciper),16)
	print("%d %d"%(x,y))
	print('----------解密')
	plain = decrypt_string(ciper,key)
	print("|%s|"%plain)

def test_des():
	a = input('请输入明文: |')

def des_alone_run():
	""" 运行DES 加密与解密
	"""
	choice = 0
	while True:
		choice = int(input("单独运行DES: 1:加密 | 2:解密 | 3:帮助 | 0:退出  ?:"))
		if choice == 1:
			plaintext = input("> 输入明文: |")
			key = input("> 输入密钥: |")
			try:
				ciper = des_encrypt(plaintext,key)
				print("加密结果: |{}|".format(ciper))
			except Exception as e:
				print("[!] 不合法输入,请查看帮助 3")
		elif choice == 2:
			ciper = input("> 输入密文: |")
			key = input("> 输入密钥: |")
			try:
				plaintext = des_decrypt(ciper,key)
				print("解密结果: |{}|".format(plaintext))
			except Exception as e:
				print("[!] 不合法输入,请查看帮助 3")
		elif choice == 3:
			print("Usage:")
			print("明文: 16位的十六进制字符串,如: abcde12119600000")
			print("密钥: 16位的十六进制字符串,如: 0123456789ABCDEF ")
			print("密文: 请输入正确密文")

		elif choice == 0:
			print('退出单独运行DES\n\n')
			break
		else:
			print('[!] 不合法输入')
			continue


def main():
	""" 运行DES 加密与解密字符串
	"""
	choice = 0
	print('\n----------------------------------')
	print("-------- DES加密解密 -------------")
	while True:
		choice = int(input("请选择功能: 1:加密 | 2:解密 | 3:单独运行DES | 0:退出  ?:"))
		if choice == 1:
			plaintext = input("输入明文: |")
			key = input("输入密钥: |")
			key = key_generator(key)
			ciper = encrypt_string(plaintext,key)
			print("加密结果: |{}|\n".format(ciper))
		elif choice == 2:
			ciper = input("输入密文(注意空格): |")
			key = input("输入密钥: |")
			key = key_generator(key)
			plaintext = decrypt_string(ciper,key)
			print("解密结果: |{}|\n".format(plaintext))
		elif choice == 3:
			print('\n----------------------------------')
			print("-------- 单独运行DES -------------")
			des_alone_run()
		elif choice == 0:
			print('选择退出')
			break
		else:
			print('不合法输入')
			continue


if __name__ == '__main__':
	# test_keys()
	main()
	# test()

	
	