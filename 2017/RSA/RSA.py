#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: RSA加解密算法
'''

"""
- 步骤
1，选取两个素数 p,q
2，计算 n = pq, fi(n) = (p-1)(q-1)
3，随机选择正整数 1 < e < fi(n), 满足 gcd(e,fi(n)) = 1
4，计算 d, 满足 de = t* fi(n) + 1  即 de = 1(mod fi(n))
5，完成加密功能
6，完成解密功能
"""

class RSA():
	"""docstring for RSA"""
	def __init__(self):
		self.p = 0
		self.q = 0
		self._set_p_q()

		self.n = self.p * self.q
		self.fin = (self.p-1) * (self.q-1)
		self.e = self._set_e()
		self.d = self._set_d()
	
	def _set_p_q(self):
		""" 设置p, 通过
		"""
		self.p = 53
		self.q = 61

	def _set_e(self):
		""" 计算公钥 e
		"""
		for e in range(2,self.fin):
			if self._gcd(e,self.fin) == 1:
				return e

	def get_e(self):
		"""	获取公钥 e
		"""
		return self.e

	def _set_d(self):
		""" 计算私钥 d
			de = 1(mod fi(n))
		"""
		for t in range(self.n):
			d,r = divmod(t*self.fin+1,self.e)
			if r == 0:
				return d

	def get_d(self):
		""" 获取私钥 d 
		"""
		return self.d

	def _gcd(self,a,b):
		""" 计算最大公约数 gcd
		利用辗转相除法: gcd(a,b) = gcd(b,a mod b)
		"""
		if a < b:
			a,b = b,a
		if a%b == 0:
			return b
		else:
			return self._gcd(b,a%b)

	def encrypt_digit(self,m):
		""" 对数字进行加密
		:Param
			- m 要进行加密的明文 ,格式为整数如 m =  '1'
		"""
		m = int(m)
		c = (m ** self.e) % self.n
		return c

	def decrypt_digit(self,c):
		""" 对数字进行解密
		:Param
			- c: 要进行解密的密文, 格式为整数 c = '2'
		"""
		c = int(c)
		m = (c ** self.d) % self.n
		return m

	def test(self):
		print(self.p,self.q,self.n,self.fin,self.e,self.d)
		m = 100
		a = self.encrypt_digit(m)
		b = self.decrypt_digit(a)
		print(m,a,b)


	def encrypt(self,plaintext):
		""" 加密一串明文,将会转为ASCII码,再进行加密,返回字符串
		"""
		cipher = []
		for s in plaintext:
			c = self.encrypt_digit(ord(s))
			cipher.append(str(c))
		return ','.join(cipher)

	def decrypt(self,cipher):
		""" 解密一串密文,先转为ASCIＩ码，在进行解密操作，返回字符串
		"""
		plaintext = []
		cipher = cipher.split(',')
		for c in cipher:
			c = int(c)
			m = self.decrypt_digit(c)
			plaintext.append(chr(m))
		return ''.join(plaintext)


if __name__ == '__main__':
	rsa = RSA()
	# rsa.test()
	m = 'hello world'
	for i in m:
		print('%d ' % ord(i),end='')
	print('\n')
	c = rsa.encrypt(m)
	p = rsa.decrypt(c)
	print(m)
	print(c)
	print(p)

