import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

from sklearn.naive_bayes import MultinomialNB
from sklearn.model_selection import train_test_split
from flask import Flask, request, jsonify

app = Flask(__name__)



@app.route('/predict', methods=['POST'])
def index():
	lines = request.get_json().get("data")
	lines=str(lines).strip(' ')
	print(lines)
	strr=lines[:]
	strr=strr.replace('[','')
	strr=strr.replace(']','')
	strr=strr.split(',')
	
	print(strr)
	print(strr[0])
	print(type(strr))
	data = pd.read_csv('csvfile.csv')
	#print(data.shape)
	data = data.fillna(0)
	data.head(5)
	#print(data.info())

	cols = data.columns.tolist()
	#print("Colsssssssssssssssssssssssssss")
	#print(cols)
	cols.remove('disease')
	x = data[cols]
	y = data.disease
	#print(x)
	#print(y)

	x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.33, random_state=42)

	mnb = MultinomialNB()
	mnb = mnb.fit(x_train, y_train)


	#print("trainnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn")
	#print(x_train)
	#print("222222222222222222222222")
	#print(y_train)
	mnb.score(x_test, y_test)
	#print("testing data...............................................")
	#print(x_test)
	#print("dddd")
	#print(y_test)

	mnb_tot = MultinomialNB()
	mnb_tot = mnb_tot.fit(x, y)
	mnb_tot.score(x, y)

	r=x.loc[x['abortion'] == 1]
	durvesh=pd.DataFrame()

	
	for sym in strr:
		w=x.loc[x[sym.strip()] == 1]
		durvesh=durvesh.append(w)
		print(w)
	print("huhuhuhuh")
	print(durvesh)

		


	

	disease_pred = mnb_tot.predict(durvesh)
	print(disease_pred)

	main = {"disease":str(disease_pred)}



	return jsonify(main)




if __name__ == '__main__':
	app.run(host='192.168.43.204',port=5051)
   