python 实现路由器转发模型
---

## FILE 
	host.py
## Describe
	模拟一台主机运行
## Usage
    python3 host.py [host_num]
    [host_num]: 输入 1-3 的数字分别对应主机1/2/3

## FILE
    route.py
## Describe
    模拟一台路由器运行
## Usage
    python3 route.py [route_num]
    [route_num]: 输入 1-3 的数字，分别对应路由器1/2/3
## Default
    默认下，路由器的转发是按着顺序 R1 -> R2 -> R3 或者 R3 -> R2 -> R1
    如果需要修改，要修改路由表
