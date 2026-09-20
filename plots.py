import os
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("results/results.csv")
os.makedirs("docs/plots", exist_ok=True)

r = df[df.inputType == "Random"]

for col, title, name in [("timeMs", "Time (ms)", "time_vs_n"),
                         ("maxDepth", "Max recursion depth", "depth_vs_n")]:
    plt.figure(figsize=(8, 5))
    for algo, d in r.groupby("algorithm"):
        d = d.sort_values("n")
        plt.plot(d.n, d[col], marker="o", label=algo)
    plt.xscale("log")
    if col == "timeMs":
        plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel(title)
    plt.title(title + " vs n (Random input)")
    plt.grid(True, alpha=0.3)
    plt.legend()
    plt.savefig(f"docs/plots/{name}.png", dpi=150)
    plt.close()

print("Plots saved to docs/plots")