#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

m = "thetragedyofjuliuscaesar"
c = "jwmewrboyafegjbxcdhrzcvt"


a = "qpilfrvkvcrzxp"


f = 'dbyn_yatji_mlelqdp3nlqg8_t7k0c'
f1 = 'dbynyatjimlelqdpnlqgt7kc'

print(len(m),len(c),len(f))

print(m)
print(c)

for i in range(len(m)):
	print chr(97+ (ord(c[i]) - ord(m[i])+26)%26),


print '\n--------------------------------'
print '\n维吉尼亚解密密秘钥:'
print a

l = len(a)
print("\n\n明文加密成密文")
for i in range(len(m)):
	tmp = i%l
	print chr(97 +  (ord(m[i]) - ord('a') + ord(a[tmp])-ord('a'))%26 ),

print('\n密文还原成明文')
for i in range(len(m)):
	tmp = i%l
	print chr(97+ (ord(c[i]) - ord(a[tmp])+26)%26),



