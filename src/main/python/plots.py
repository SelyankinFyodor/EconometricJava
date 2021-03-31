import matplotlib.pyplot as plt
import matplotlib.ticker as ticker


S0 = []
S1 = []
S2 = []
S3 = []
inds = []

filenames = ["Zero", "First", "Second", "Third"]
sigmaDir = "../../../dataFiles/Sigma/"
ext = ".txt"

f = open(sigmaDir + filenames[3] + ext)
i = 0
while True:
	line = f.readline()
	if not line:
		break
	ind, s0, s1, s2, s3 = line.split()
	if i % 100 == 0:
		inds.append(ind)
		S0.append(float(s0))
		S1.append(float(s1))
		S2.append(float(s2))
		S3.append(float(s3))
	i += 1
	print(i)
fig, ax = plt.subplots()
ax.plot(inds, S1, "r-")
ax.plot(inds, S1, "b-")
ax.plot(inds, S2, "g-")
ax.plot(inds, S3, "y-")
ax.set_xlabel("номер интервала")
ax.set_ylabel("сигма")
ax.xaxis.set_major_locator(ticker.MultipleLocator(1000))
ax.legend(["Zero", "First", "Second", "Third"])
# ax.yaxis.set_major_locator(ticker.MultipleLocator(1000))
plt.show()
