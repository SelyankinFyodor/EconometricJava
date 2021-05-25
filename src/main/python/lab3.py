import matplotlib.pyplot as plt
import matplotlib.ticker as ticker


T = []
X = []
iterations = []

ext = ".txt"

f = open("../../../dataFiles/lab3/Expression.txt")
i = 0
line = f.readline()
while True:
	line = f.readline()
	if not line:
		break
	t, x = line.split()
	T.append(float(t))
	X.append(float(x))
	i += 1
fig, ax = plt.subplots()
ax.plot(T, X, "r-")
f = open("../../../dataFiles/lab3/estimate.txt")
i = 0
line = f.readline()
T = []
X = []
while True:
	line = f.readline()
	if not line:
		break
	t, x = line.split()
	T.append(float(t))
	X.append(float(x))
	i += 1
ax.plot(T, X, "g-")
ax.set_xlabel("T")
ax.set_ylabel("X")
plt.show()
