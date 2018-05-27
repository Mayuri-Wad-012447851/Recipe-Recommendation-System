
# coding: utf-8

# In[2]:


import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns


# In[3]:


#read input file
r =pd.read_json("full_format_recipes.json")
recipe = r[['title','rating', 'calories', 'protein', 'fat', 'sodium', 'categories']]


# In[4]:


recipe.head(5)


# In[5]:


recipe["calories"].fillna(0, inplace=True)
recipe["protein"].fillna(0, inplace=True)
recipe["fat"].fillna(0, inplace=True)
recipe["sodium"].fillna(0, inplace=True)


# In[6]:


recipe.head(10)


# In[7]:


#Plot Calories against Ratings
sns.set(font_scale=2)

fig, ax = plt.subplots(figsize=(10,10))


sns.barplot(x='rating',y='calories', data=recipe, errwidth =0)
ax.set_title('Rating vs Calories', size=30)
ax.set_ylabel('Calories')
ax.set_xlabel('Rating')

plt.show()


# In[8]:


#Plot Protein against Ratings
sns.set(font_scale=2)

fig, ax = plt.subplots(figsize=(10,10))


sns.barplot(x='rating',y='protein', data=recipe, errwidth =0)
ax.set_title('Rating vs Protein', size=30)
ax.set_ylabel('Protein')
ax.set_xlabel('Rating')

plt.show()


# In[9]:


#Plot Fat against Ratings
sns.set(font_scale=2)

fig, ax = plt.subplots(figsize=(10,10))


sns.barplot(x='rating',y='fat', data=recipe, errwidth =0)
ax.set_title('Rating vs Fat', size=30)
ax.set_ylabel('Fat')
ax.set_xlabel('Rating')

plt.show()


# In[10]:


#Plot Sodium against Ratings
sns.set(font_scale=2)

fig, ax = plt.subplots(figsize=(10,10))


sns.barplot(x='rating',y='sodium', data=recipe, errwidth =0)
ax.set_title('Rating vs Sodium', size=30)
ax.set_ylabel('Sodium')
ax.set_xlabel('Rating')

plt.show()


# In[35]:


#Plot recipes with the most ratings
recipe1 = recipe[['title','rating']]

top_recipes = recipe1.title.value_counts()[:10]
plt.figure(figsize=(12, 8))
pos = np.arange(len(top_recipes))
plt.barh(pos, top_recipes.values);
plt.yticks(pos, top_recipes.index);
plt.title('Recipes with the most ratings')
plt.xlabel('Rating Count')
plt.ylabel('Title')


# In[36]:


top_recipes


# In[37]:


plt.show()


# In[52]:


re =  recipe1['rating'].value_counts()
re


# In[57]:


#Number of recipes per Rating
plt.figure(figsize=(12, 8))
pos = np.arange(len(re))
plt.barh(pos, re.values);
plt.yticks(pos, re.index);
plt.title('Number of Recipes per Rating')
plt.xlabel('Number of Recipes')
plt.ylabel('Rating')


# In[58]:


plt.show()

