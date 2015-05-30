# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('explora_app', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='volunteer_data',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('number', models.CharField(max_length=50)),
                ('longitude', models.CharField(max_length=25)),
                ('latitude', models.CharField(max_length=25)),
            ],
        ),
    ]
