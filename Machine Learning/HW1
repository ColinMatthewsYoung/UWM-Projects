from sklearn import datasets
from sklearn.tree import DecisionTreeClassifier
from sklearn import metrics
from sklearn import model_selection
from sklearn.metrics import roc_curve
from matplotlib import pyplot as plt
from sklearn.metrics import roc_auc_score

def main():

    print("Fetching dataset id: 45090")
    diaLeukemia = datasets.fetch_openml(data_id=45090)
    print("Fetching dataset id: 1132")
    diaLung = datasets.fetch_openml(data_id=1132)

    dSets = [diaLeukemia,diaLung]
    for x in dSets:
        parameters = [{"min_samples_leaf": [2, 4, 6, 8, 10]}]
        dtcEntropy = DecisionTreeClassifier(criterion="entropy")
        dtcGini = DecisionTreeClassifier()
        tuned_dtcEntropy = model_selection.GridSearchCV(dtcEntropy, parameters, scoring="roc_auc", cv=5)
        tuned_dtcGini = model_selection.GridSearchCV(dtcGini, parameters, scoring="roc_auc", cv=5)

        print("starting Entropy Cross-Validation for " + x.details["name"])
        y_scoresE = model_selection.cross_val_predict(tuned_dtcEntropy, x.data, x.target, method="predict_proba", cv=10)
        print("starting Gini Cross-Validation for " + x.details["name"])
        print('\n')
        y_scoresG = model_selection.cross_val_predict(tuned_dtcGini, x.data, x.target, method="predict_proba", cv=10)
        if x is dSets[1]:
            label = "Omentum"
        else:
            label = "AML"

        fprE, tprE, thE = roc_curve(x.target, y_scoresE[:, 1], pos_label=label)
        fprG, tprG, thG = roc_curve(x.target, y_scoresG[:, 1], pos_label=label)

        plt.title(x.details["name"])
        plt.xlabel("1 - Specificity")
        plt.ylabel("Sensitivity")
        plt.xlim(0, 1)
        plt.ylim(0, 1)
        plt.plot(fprE, tprE, label="Decision Tree Entropy")
        plt.plot(fprG, tprG, label="Decision Tree Gini")
        plt.legend()
        plt.show()

        print("AUC Entropy tree:")
        print(roc_auc_score(x.target, y_scoresE[:, 1]))
        print("\n")
        print("AUC Gini tree:")
        print(roc_auc_score(x.target, y_scoresG[:, 1]))
        print("\n")


    input("press any key to exit.")

    return 0

if __name__ == '__main__':
    main()

