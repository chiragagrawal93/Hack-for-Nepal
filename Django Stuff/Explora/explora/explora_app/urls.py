from django.conf.urls import url

from . import views


urlpatterns = [
	url(r'^$', views.index,name='index'),
	url(r'^data/(?P<number>[0-9]{10})/(?P<latitude>[0-9]{2}[.][0-9]+)/(?P<longitude>[0-9]{2}[.][0-9]+)/(?P<state>\w+)/$', views.send_victim_data,name='send_victim_data'),
	url(r'^register/(?P<name>\w+)/(?P<number>[0-9]{10})/$', views.register_user,name='register_user'),
	url(r'^update/(?P<number>[0-9]{10})/(?P<latitude>[0-9]{2}[.][0-9]+)/(?P<longitude>[0-9]{2}[.][0-9]+)/$', views.update_volunteer_position,name='volunteer_data_update'),
	url(r'^get/volunteer/(?P<latitude>[0-9]{2}[.][0-9]+)/(?P<longitude>[0-9]{2}[.][0-9]+)/$',views.get_closest_volunteer,name='get_closest_volunteer'),
	]


