#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
利用 Kasiski 测试法（Kasiski test）和重合指数法（index of coincidence），
对一段明文经 Vigenere 密码加密后的密文进行解密。
并求出 Mg 值
'''


# 步骤如下：
# 1，设某一密文片段重复出现的位置为 X1,X2,X3..., 题目使用子串 'CHR'
# 2，则密文长度可估计为 m = gcd(X1,X2,X3,...) , 题目目测长度为 5
# 3，接着由 Mg 的公式，遍历出 Yi (0< i <= m) 的 Mg 值的表
# 4，将得到表，找出 Yi 下，与 0.065 最近的值，并获取它在该表的位置 Wi
# 5，Wi 所对应的英文字母即为密钥


import math

# -- config start --
M_LEN = 5
CIPHERTEXT =""" CHREEVOAHMAERATBIAXXWTNXBEEOPHBSBQMQEQERBW
				RVXUOAKXAOSXXWEAHBWGJMMQMNKGRFVGXWTRZXWIAK
				LXFPSKAUTEMNDCMGTSXMXBTUIADNGMGPSRELXNJELX
				VRVPRTULHDNQWTWDTYGBPHXTFALJHASVBFXNGLLCHR
				ZBWELEKMSJIKNBHWRJGNMGJSGLXFEYPHAGNRBIEQJT
				AMRVLCRREMNDGLXRRIMGNSNRWCHRQHAEYEVTAQEBBI
				PEEWEVKAKOEWADREMXMTBHHCHRTKDNVRZCHRCLQOHP
				WQAIIWXNRMGWOIIFKEE
			"""

LETTER_P={
	'a':0.082,'b':0.015,'c':0.028,'d':0.043,'e':0.127,'f':0.022,
	'g':0.020,'h':0.061,'i':0.070,'j':0.002,'k':0.008,'l':0.040,
	'm':0.024,'n':0.067,'o':0.075,'p':0.019,'q':0.001,'r':0.060,
	's':0.063,'t':0.091,'u':0.028,'v':0.010,'w':0.023,'x':0.001,
	'y':0.020,'z':0.001

}

# -- config end

def get_Mg(ciphertext=CIPHERTEXT,mlen=M_LEN):
	# 去空格，去换行符，去 tab
	ciphertext = ciphertext.strip().replace('\n','').replace('\t','').lower()
	sub_text = []
	# 将密文分成m行，每行有 n/m 列,
	# 第一行为 1，m+1,2m+1,... h%m=1
	# 第二行为底数余 2
	# 以此类推，第 m 行的底数余 m
	sub_len = int(math.ceil(float(len(ciphertext))/mlen))
	sub_text = {}
	text = []
	for i in range(len(ciphertext)):
		m = i % mlen
		text = sub_text.get(m,[])
		text.append(ciphertext[i])
		sub_text[m] = text
	

	# 获取各行子串中，各个字母出现的次数
	counts = {}
	for i in range(mlen):
		set_text = set(sub_text[i])
		count = {}
		for t in set_text:
			count[t] = sub_text[i].count(t)
		counts[i] = count


	# 计算 Mg
	Mgs = []
	for m in range(mlen):
		sub_Mg=[]
		for g in range(26):
			Mg = 0.0
			for letter,pi in LETTER_P.items():
				i = ord(letter)-ord('a')
				key = chr((i+g)%26 + ord('a'))
				F = counts[m].get(key,0)
				# (Pi * Fi+g) / n 
				Mg += float(pi * F) / len(sub_text[m])
					
					
			sub_Mg.append(round(Mg,3))
		Mgs.append(sub_Mg)	

	# print (Mgs)
	for mg in Mgs:
		for i in range(26):
			print ('%.3f ' % mg[i],end='')
			if (i+1)%9 == 0:
				print("\n")
		print('\n*********')


def test(ciphertext=CIPHERTEXT):
	ciphertext = ciphertext.strip().replace('\n','').replace('\t','').lower()
	set_text = set(ciphertext)
	n = len(ciphertext)
	tmp = 0
	for t in set_text:
		count = ciphertext.count(t)
		tmp += float(count * (count-1)) / (n*(n-1))

	print(round(tmp,3))



if __name__ == '__main__':
	get_Mg(CIPHERTEXT)
	# test()