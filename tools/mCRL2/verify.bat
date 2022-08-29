mcrl22lps mcrl2file.mcrl2 --noalpha
lps2pbes mcrl2file.lps --formula="formula.mcf"
pbes2bool mcrl2file.pbes --evidence-file="result.txt" --file="mcrl2file.lps"