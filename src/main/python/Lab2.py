import matplotlib.pyplot as plt
import matplotlib.ticker as ticker


Temperatures = []
Sigmas = []
iterations = []

sigmaDir = "../../../dataFiles/lab2/Samples/SigmaFileName"
ext = ".txt"

f = open("../../../dataFiles/lab2/Samples/IterSigmaTempFileName.txt")
i = 0
line = f.readline()
while True:
	line = f.readline()
	if not line:
		break
	iter, sigma, temperature = line.split()
	iterations.append(int(iter))
	Temperatures.append(float(temperature))
	Sigmas.append(float(sigma))
	i += 1
	print(i)
fig, ax = plt.subplots()
ax.plot(iterations, Sigmas, "r-")
ax.set_xlabel("число итераций")
ax.set_ylabel("сигма")


fig, ax = plt.subplots()
ax.plot(Temperatures, Sigmas, "r-")

ax.set_xlabel("Температура")
ax.set_ylabel("сигма")
plt.show()
