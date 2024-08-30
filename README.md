# Prognostica

## Overview

Prognostica is a Flask-based web application designed for medical ailment classification. The app takes symptom data as input and predicts the likely disease using a Naive Bayes classifier. It leverages a dataset containing various symptoms and corresponding diseases, providing quick and accurate predictions.

## Features

- **Naive Bayes Classification:** The app uses a Multinomial Naive Bayes model for disease prediction based on the provided symptoms.
- **Flask API:** The application provides a RESTful API endpoint (`/predict`) to accept symptom data and return disease predictions in JSON format.
- **Data Handling:** The app processes and cleans the data, filling in missing values and preparing it for the model.

## Installation


### Step-by-Step Guide

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/prognostica.git
    cd prognostica
    ```

2. Install the required Python packages:

    ```bash
    pip install -r requirements.txt
    ```

3. Place your symptom dataset in the root directory as `csvfile.csv`.

4. Run the application:

    ```bash
    python server.py
    ```

    The app will start running on the local server (`192.168.43.204`) at port `5051`.

## Usage

### API Endpoint

- **POST `/predict`:** This endpoint accepts a JSON object with the key `"data"` containing a list of symptoms.

    **Request Example:**
    ```json
    {
        "data": ["fever", "cough", "headache"]
    }
    ```

    **Response Example:**
    ```json
    {
        "disease": "[Malaria]"
    }
    ```
