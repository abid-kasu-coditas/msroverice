INSERT INTO states (id, name, code) VALUES
  (1, 'Maharashtra', 'MH'),
  (2, 'Gujarat', 'GJ')
ON CONFLICT (id) DO NOTHING;

INSERT INTO districts (id, state_id, name) VALUES
  (1, 1, 'Mumbai Suburban'),
  (2, 1, 'Pune'),
  (3, 2, 'Ahmedabad')
ON CONFLICT (id) DO NOTHING;

INSERT INTO cities (id, district_id, name) VALUES
  (1, 1, 'Mumbai'),
  (2, 2, 'Pune'),
  (3, 3, 'Ahmedabad')
ON CONFLICT (id) DO NOTHING;

INSERT INTO areas (id, city_id, name, pincode) VALUES
  (1, 1, 'Andheri East', '400069'),
  (2, 1, 'Borivali West', '400092'),
  (3, 2, 'Hinjewadi', '411057'),
  (4, 3, 'Navrangpura', '380009')
ON CONFLICT (id) DO NOTHING;
