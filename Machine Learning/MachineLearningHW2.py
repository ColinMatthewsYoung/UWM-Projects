from sklearn.preprocessing import OneHotEncoder
from sklearn import datasets
from sklearn.compose import ColumnTransformer
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import learning_curve
from sklearn.neighbors import KNeighborsRegressor
from sklearn.tree import DecisionTreeRegressor
from matplotlib import pyplot as plt


def main():

    #get data, store in lis.
    #42726: 9 feat, 1 sym, 4177 inst
    print("fetching dataset 42726")
    lis = datasets.fetch_openml(data_id=42726)

    #convert nominal features into numerical valuse using onehotencoder and columntransformer.
    ohe = OneHotEncoder(sparse_output=False)
    nominalArray = []
    #find each dtype with nominal array, save index for columnTransfomer
    y = 0
    for x in lis.data.dtypes:
        if (x == 'object' or x == 'category'):
            nominalArray.append(y)
        y+=1
    ct = ColumnTransformer([("encoder", OneHotEncoder(sparse_output=False), nominalArray)], remainder="passthrough")
    new_data = ct.fit_transform(lis.data)

    #k-nearest, 3 k values:
    k = [1,5,13]
    print("training k-nearest neighbor model where k = 1,5,13")
    kn_3_examples, kn_3_rmse= kNearest(new_data,lis,k)

    #k-nearest neighbor, 5 k values:
    k = [3,7,9,15,21]
    print("training k-nearest neighbor model where k = 3,7,9,15,21")
    kn_5_examples, kn_5_rmse = kNearest(new_data,lis,k)

    print("training decision tree regression model")
    dct_examples, dct_rmse = decisionTree(new_data, lis)

    print("training linear regression model")
    lr_examples, lr_rmse = linearRegression(new_data, lis)


    #plot each method, 10 versions.
    plot(kn_3_examples, kn_3_rmse, "Kn 3")
    plot(kn_5_examples,kn_5_rmse, "Kn 5")
    plot(lr_examples, lr_rmse, "Lr")
    plot(dct_examples, dct_rmse, "DCT")

    plotAll(kn_5_examples,kn_5_rmse,"Kn5", lr_examples, lr_rmse,"LR",dct_examples,dct_rmse,"DCT", "Learning curve mean comparison")



    input("press any key to exit.")


def kNearest(new_data,lis,k):
    parameter_grid = [{"n_neighbors": k}]
    tuned_knn = GridSearchCV(KNeighborsRegressor(), parameter_grid, scoring="neg_root_mean_squared_error", cv=5)
    training_examples, training_scores, test_scores = learning_curve(tuned_knn, new_data, lis.target,
                                                                     train_sizes=[0.1, 0.2, 0.4, 0.6, 0.8, 1.0], cv=10,
                                                                     scoring="neg_root_mean_squared_error")

    return training_examples,0-test_scores

def decisionTree(new_data, lis):
    parameters = [{"min_samples_leaf": [2, 4, 6, 8, 10]}]
    dtc = DecisionTreeRegressor()
    tuned_dtc = GridSearchCV(dtc, parameters, scoring="neg_root_mean_squared_error", cv=5)
    training_examples, training_scores, test_scores = learning_curve(tuned_dtc, new_data, lis.target,
                                                                     train_sizes=[0.1, 0.2, 0.4, 0.6, 0.8, 1.0], cv=10,
                                                                     scoring="neg_root_mean_squared_error")
    return training_examples, 0 - test_scores

def linearRegression(new_data, lis):
    lr = LinearRegression()
    training_examples, training_scores, test_scores = learning_curve(lr, new_data, lis.target,
                                                                     train_sizes=[0.1, 0.2, 0.4, 0.6, 0.8, 1.0], cv=10,
                                                                     scoring="neg_root_mean_squared_error")

    return training_examples, 0-test_scores


def plot(training_examples, rmse, title):
    plt.title(title)
    plt.xlabel("training examples")
    plt.ylabel("RMSE")
    plt.xlim()
    i=0
    while (i<10):
        plt.plot(training_examples, rmse[:, i], label=i, marker='o')
        i+=1

    cell_text = []
    for i in rmse[5, :]:
        cell_text.append(f'{i :.3f}')
    plt.table(cellText=[cell_text], rowLabels=[training_examples.max()],colLabels=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9], loc='bottom',
              bbox=[0.0,-0.45,1,.28])

    plt.subplots_adjust(bottom=0.3)
    plt.legend()
    plt.show()
    return

def plotAll(examples1, rmse1, LearningTitle1, examples2, rmse2,LearningTitle2, examples3, rmse3,LearningTitle3, title):
    plt.title(title)
    plt.xlabel("training examples")
    plt.ylabel("RMSE")
    plt.xlim()
    plt.plot(examples1, rmse1.mean(axis=1), label=LearningTitle1, marker='o')
    plt.plot(examples2, rmse2.mean(axis=1), label=LearningTitle2, marker='o')
    plt.plot(examples3, rmse3.mean(axis=1), label=LearningTitle3, marker='o')

    rmse1Text = []
    for i in rmse1[5, :]:
        rmse1Text.append(f'{i :.3f}')
    mean = str(rmse1.mean(axis=1)[5:6])
    mean = mean[1:6]
    rmse1Text.append(mean)

    rmse2Text = []
    for i in rmse2[5, :]:
        rmse2Text.append(f'{i :.3f}')
    mean = str(rmse2.mean(axis=1)[5:6])
    mean = mean[1:6]
    rmse2Text.append(mean)

    rmse3Text = []
    for i in rmse3[5, :]:
        rmse3Text.append(f'{i :.3f}')
    mean = str(rmse3.mean(axis=1)[5:6])
    mean = mean[1:6]
    rmse3Text.append(mean)

    rl = ['Kn5','LR','DCT']
    cell_text = [rmse1Text,rmse2Text,rmse3Text]
    plt.table(cellText=cell_text, rowLabels=rl, colLabels=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9,'Mean'],
              loc='bottom',
              bbox=[0.0, -0.45, 1, .28])

    plt.subplots_adjust(bottom=0.3)

    plt.legend()
    plt.show()


if __name__ == '__main__':
    main()
