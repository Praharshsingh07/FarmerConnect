package com.suretrust.farmerconnect;


    public class userData {
        String name,number,email;

        public userData() {
        }



        public userData(String name, String number,String email) {
            this.name = name;
            this.number = number;
            this.email = email;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

