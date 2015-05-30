from django.db import models

class location_data(models.Model):
	name=models.CharField(max_length=200)
	latitude=models.CharField(max_length=50)
	longitude=models.CharField(max_length=50)

class user_data(models.Model):
	user_name=models.CharField(max_length=200)
	user_phone=models.CharField(max_length=50)
	
class victim_data(models.Model):
	number=models.CharField(max_length=50)
	latitude=models.CharField(max_length=25)
	longitude=models.CharField(max_length=25)
	state=models.BooleanField(default=True)
	
class volunteer_data(models.Model):
	number=models.CharField(max_length=50)
	longitude=models.CharField(max_length=25)
	latitude=models.CharField(max_length=25)
