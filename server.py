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
	request_str=lines[:]
	request_str=request_str.replace('[','')
	request_str=request_str.replace(']','')
	request_str=request_str.split(',')
	
	print(request_str)
	print(request_str[0])
	print(type(request_str))
	data = pd.read_csv('csvfile.csv')
	#print(data.shape)
	data = data.fillna(0)
	data.head(5)
	#print(data.info())

	cols = data.columns.tolist()
	#print(cols)
	cols.remove('disease')
	x = data[cols]
	y = data.disease
	#print(x)
	#print(y)

	x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.33, random_state=42)

	mnb = MultinomialNB()
	mnb = mnb.fit(x_train, y_train)



	#print(x_train)

	#print(y_train)
	mnb.score(x_test, y_test)

	#print(x_test)

	#print(y_test)

	mnb_tot = MultinomialNB()
	mnb_tot = mnb_tot.fit(x, y)
	mnb_tot.score(x, y)

	r=x.loc[x['abortion'] == 1]
	data=pd.DataFrame()

	
	for sym in request_str:
		w=x.loc[x[sym.strip()] == 1]
		data=data.append(w)
		print(w)
	print(data)

		


	

	disease_pred = mnb_tot.predict(data)
	print(disease_pred)

	main = {"disease":str(disease_pred)}



	return jsonify(main)




if __name__ == '__main__':
	app.run(host='192.168.43.204',port=5051)
   
