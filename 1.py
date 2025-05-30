# models.py
# Слой моделей и БД для ANIMA с использованием Peewee ORM
from peewee import Model, CharField, TextField, DateTimeField, IntegerField, BooleanField, SqliteDatabase, ForeignKeyField
from datetime import datetime

# Инициализация базы данных SQLite (локально)
db = SqliteDatabase('anima.db')

class BaseModel(Model):
    class Meta:
        database = db

class User(BaseModel):
    id = IntegerField(primary_key=True)
    username = CharField(unique=True)
    email = CharField(unique=True)
    created_at = DateTimeField(default=datetime.now)
    # психопрофиль
    phq9_score = IntegerField(null=True)
    gad7_score = IntegerField(null=True)
    diagnosis = TextField(null=True)
    culture = CharField(null=True)
    country = CharField(null=True)

class Task(BaseModel):
    id = IntegerField(primary_key=True)
    user = ForeignKeyField(User, backref='tasks')
    title = CharField()
    category = CharField()  # cognitive, behavioral, meditative, social
    description = TextField(null=True)
    points = IntegerField(default=0)
    date_assigned = DateTimeField(default=datetime.now)
    date_completed = DateTimeField(null=True)
    completed = BooleanField(default=False)

class StateRecord(BaseModel):
    id = IntegerField(primary_key=True)
    user = ForeignKeyField(User, backref='states')
    date = DateTimeField(default=datetime.now)
    mood = IntegerField(null=True)          # 1-10
    anxiety = IntegerField(null=True)       # 1-10
    sleep_quality = IntegerField(null=True) # 1-10
    productivity = IntegerField(null=True)  # 1-10

class Report(BaseModel):
    id = IntegerField(primary_key=True)
    user = ForeignKeyField(User, backref='reports')
    generated_at = DateTimeField(default=datetime.now)
    file_path = CharField()  # путь к PDF-файлу

# Функция инициализации БД
def initialize_database():
    db.connect()
    db.create_tables([User, Task, StateRecord, Report])
    db.close()

if __name__ == '__main__':
    initialize_database()
    print('Database initialized with tables: User, Task, StateRecord, Report')
# 1.py
# ANIMA application: models, database, and Kivy UI in one file

from peewee import Model, CharField, TextField, DateTimeField, IntegerField, BooleanField, SqliteDatabase, ForeignKeyField
from datetime import datetime
from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.metrics import dp

# --- Database setup ---
db = SqliteDatabase('anima.db')

class BaseModel(Model):
    class Meta:
        database = db

class User(BaseModel):
    id = IntegerField(primary_key=True)
    username = CharField(unique=True)
    email = CharField(unique=True)
    created_at = DateTimeField(default=datetime.now)
    phq9_score = IntegerField(null=True)
    gad7_score = IntegerField(null=True)
    diagnosis = TextField(null=True)
    culture = CharField(null=True)
    country = CharField(null=True)

class Task(BaseModel):
    id = IntegerField(primary_key=True)
    user = ForeignKeyField(User, backref='tasks')
    title = CharField()
    category = CharField()  # cognitive, behavioral, meditative, social
    description = TextField(null=True)
    points = IntegerField(default=0)
    date_assigned = DateTimeField(default=datetime.now)
    date_completed = DateTimeField(null=True)
    completed = BooleanField(default=False)

class StateRecord(BaseModel):
    id = IntegerField(primary_key=True)
    user = ForeignKeyField(User, backref='states')
    date = DateTimeField(default=datetime.now)
    mood = IntegerField(null=True)          # 1-10
    anxiety = IntegerField(null=True)       # 1-10
    sleep_quality = IntegerField(null=True) # 1-10
    productivity = IntegerField(null=True)  # 1-10

class Report(BaseModel):
    id = IntegerField(primary_key=True)
    user = ForeignKeyField(User, backref='reports')
    generated_at = DateTimeField(default=datetime.now)
    file_path = CharField()  # path to PDF report

# Initialize database tables

def initialize_database():
    db.connect()
    db.create_tables([User, Task, StateRecord, Report])
    db.close()

# --- UI Screens ---
class MainMenuScreen(Screen): pass
class ProfileScreen(Screen): pass
class TasksScreen(Screen): pass
class RelaxationScreen(Screen): pass
class AnalyticsScreen(Screen): pass
class SupportScreen(Screen): pass
class SettingsScreen(Screen): pass

class AnimaScreenManager(ScreenManager): pass

KV = '''
AnimaScreenManager:
    MainMenuScreen:
    ProfileScreen:
    TasksScreen:
    RelaxationScreen:
    AnalyticsScreen:
    SupportScreen:
    SettingsScreen:

<MainMenuScreen>:
    name: 'main_menu'
    BoxLayout:
        orientation: 'vertical'
        spacing: dp(10)
        padding: dp(20)
        Button:
            text: 'Начать восстановление'
            on_release: root.manager.current = 'tasks'
        Button:
            text: 'Релакс-зона'
            on_release: root.manager.current = 'relax'
        Button:
            text: 'Профиль'
            on_release: root.manager.current = 'profile'
        Button:
            text: 'Аналитика'
            on_release: root.manager.current = 'analytics'
        Button:
            text: 'Поддержка'
            on_release: root.manager.current = 'support'
        Button:
            text: 'Настройки'
            on_release: root.manager.current = 'settings'

<ProfileScreen>:
    name: 'profile'
    Label:
        text: 'Профиль пользователя (здесь будет информация о прогрессе)'

<TasksScreen>:
    name: 'tasks'
    Label:
        text: 'Дневные задания и тесты'

<RelaxationScreen>:
    name: 'relax'
    Label:
        text: 'Релакс-зона: медитации и звуки'

<AnalyticsScreen>:
    name: 'analytics'
    Label:
        text: 'Аналитика состояния'

<SupportScreen>:
    name: 'support'
    Label:
        text: 'Психологическая поддержка'

<SettingsScreen>:
    name: 'settings'
    Label:
        text: 'Настройки приложения'
'''

class AnimaApp(App):
    def build(self):
        initialize_database()
        Builder.load_string(KV)
        return AnimaScreenManager()

if __name__ == '__main__':
    AnimaApp().run()