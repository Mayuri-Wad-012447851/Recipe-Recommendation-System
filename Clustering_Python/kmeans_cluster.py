import json
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.cluster import KMeans
from sklearn.metrics import pairwise_distances_argmin_min
import matplotlib.pyplot as plt

#Read JSON file
with open('full_format_recipes.json') as data_file:
    data = json.load(data_file)

#Converts categories in list to String
words = []
for item in data:
        #print item['categories']
        try:
            words.append(' '.join(item['categories']))
        except:
            print ""


#print words
#print len(data)

#Count Vector for the words
cv = CountVectorizer()
words = list(set(words))
print "Data Count"
print len(words)
#filter(None,words)
matrix = cv.fit_transform(words)

#Finds Sum of Squared Errors
inertia = []
for i in range(1,11,1):
    kmeans = KMeans(n_clusters=i)
    kmeans.fit(matrix)
    print kmeans.inertia_
    inertia.append(kmeans.inertia_)

#Plots Sum of Squared Errors to determine the best K
x_axis = [1,2,3,4,5,6,7,8,9,10]
plt.plot(x_axis, inertia,label="Sum of Squared Error")
plt.scatter(x_axis, inertia)
plt.xlabel('Number of clusters')
plt.ylabel('Sum of Squared Error')
plt.title('Sum of Squared Error for the clusters')
plt.show()

#K Means for 3 clusters
kmeans = KMeans(n_clusters=3, max_iter=15)
kmeans.fit(matrix)
centers = kmeans.cluster_centers_
print "Centroids"
print centers
print ""
labels =  (kmeans.labels_).tolist()
closest, _ = pairwise_distances_argmin_min(centers, matrix)
#print labels
for i in range(0,3,1):
    print "Data count in cluster "+str(i)
    print labels.count(i)
    print ""

print "Closest Data to centroid"
#print closest
for cls in closest:
    print words[cls]