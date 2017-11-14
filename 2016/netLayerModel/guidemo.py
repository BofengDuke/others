import tkinter

root = tkinter.Tk()
# root.geometry('200x200+100+100')
F = tkinter.Frame(root,width=550,height=500)
sb = tkinter.Scrollbar(F)


text = tkinter.Text(F,height=500)
data = "hahahhahahsdfsdfsadfsadfsaaaaaaaasdfaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
data = data+data+data+data
text.insert(tkinter.END,data)

text.configure(yscrollcommand=sb.set)
sb.config(command=text.yview)

F.grid(row=0)
# F.grid_propagate(0)
text.grid(row=0)

sb.grid(row=0,column=1,sticky='ns')

root.mainloop()