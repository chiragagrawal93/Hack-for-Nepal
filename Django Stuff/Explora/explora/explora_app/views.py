from django.shortcuts import render
from django.http import HttpResponse
from django.db import IntegrityError
from .models import location_data
from .models import user_data
from .models import victim_data
from .models import volunteer_data
import urllib
import json
import math

# Create your views here.
def index(request):
	#could be the place where the map will be seen
	return HttpResponse("Halo re Halo")
	
def send_victim_data(request,number,latitude,longitude,state):
	
	if state=="safe" or state=="SAFE":
		enter_state=True
	else:
		enter_state=False
	
	entry=victim_data(number=number,latitude=latitude,longitude=longitude,state=enter_state)
	entry.save()	
	return HttpResponse(number+" "+ latitude + " " + longitude)
	
def get_closest_volunteer(request,latitude,longitude):
	distance_to_volunteer={}
	volunteer_list=list(volunteer_data.objects.all())
	for x in range(0,len(volunteer_list)):
		long1=volunteer_list[x].longitude
		lat1=volunteer_list[x].latitude
		distance=calculate_distance(latitude,longitude,lat1,long1)
		contact_list=[]
		distance_list=[]
		contact_list.append(volunteer_list[x].number)
		distance_list.append(distance)
	max_item_index=distance_list.index(max(distance_list))
	#contact_list[max_item_index] gives us the number of the closest volunteer
	return HttpResponse(contact_list[max_item_index])	

def calculate_distance(lat1,long1,lat2,long2):
	# Convert latitude and longitude to 
    # spherical coordinates in radians.
    degrees_to_radians = math.pi/180.0
         
    # phi = 90 - latitude
    phi1 = (90.0 - float(lat1))*float(degrees_to_radians)
    phi2 = (90.0 - float(lat2))*float(degrees_to_radians)
         
    # theta = longitude
    theta1 = float(long1)*degrees_to_radians
    theta2 = float(long2)*degrees_to_radians
         
    # Compute spherical distance from spherical coordinates.
         
    # For two locations in spherical coordinates 
    # (1, theta, phi) and (1, theta, phi)
    # cosine( arc length ) = 
    #    sin phi sin phi' cos(theta-theta') + cos phi cos phi'
    # distance = rho * arc length
     
    cos = (math.sin(phi1)*math.sin(phi2)*math.cos(theta1 - theta2) + 
           math.cos(phi1)*math.cos(phi2))
    arc = math.acos( cos )
 
    # Remember to multiply arc by the radius of the earth 
    # in your favorite set of units to get length.
    return arc
	
def register_user(request,name,number):
	check=user_data.objects.filter(user_phone=number)
	if not check:
		new_user=user_data(user_name=name,user_phone=number)
		new_user.save()
		return HttpResponse("Registered Successfully " + name)
	else:
		return HttpResponse("Existing User")

def update_volunteer_position(request,number,latitude,longitude):
	update=volunteer_data.objects.filter(number=number)
	#return HttpResponse(update)
	if not update:
		new_entry=volunteer_data(number=number,latitude=latitude,longitude=longitude)
		new_entry.save()
		return HttpResponse("New " + str(new_entry))
	else:
		update_entry=volunteer_data.objects.filter(number=number).update(number=number,latitude=latitude,longitude=longitude)
		return HttpResponse("Update " + str(update_entry))
	return HttpResponse("Otherwise " + update)
	
	
		
	
#def get_victim_data(request):

	
	
	
	
