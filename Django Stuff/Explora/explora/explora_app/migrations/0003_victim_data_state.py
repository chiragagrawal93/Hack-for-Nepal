# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('explora_app', '0002_volunteer_data'),
    ]

    operations = [
        migrations.AddField(
            model_name='victim_data',
            name='state',
            field=models.BooleanField(default=True),
        ),
    ]
