from django.contrib import admin
from .models import location_data
from .models import user_data
from .models import victim_data


admin.site.register(user_data)
admin.site.register(victim_data)
# Register your models here.
