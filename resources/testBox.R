setwd("C:/Users/luisb/Desktop/TFG/TFG-HerramientaAlgEvolutivos/resources")
library(readxl)

exp <- read.csv("C:/Users/luisb/Desktop/TFG/TFG-HerramientaAlgEvolutivos/resources/resumen.csv", sep=";")
pdf("pruebaBoxPlot.pdf")
boxplot(exp$averageFitness, names = c("AverageFitness"), xlab = "Cardinality")
dev.off()
